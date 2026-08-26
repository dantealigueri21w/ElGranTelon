package pe.appmobile.elgrantelon.data.repository

import pe.appmobile.elgrantelon.data.AppDatabase
import pe.appmobile.elgrantelon.data.entity.CartelEntity
import pe.appmobile.elgrantelon.data.entity.IntentoDeclamacionEntity
import pe.appmobile.elgrantelon.data.entity.MedallaEntity
import pe.appmobile.elgrantelon.data.entity.PerfilEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.data.entity.RachaEntity
import pe.appmobile.elgrantelon.domain.engine.MotorProgreso
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento
import java.time.LocalDate

data class ResultadoRegistroIntento(
    val medallasNuevas: Set<String>,
    val actoDesbloqueadoSiguiente: Boolean,
    val cartelNuevo: Boolean
)

class ElGranTelonRepository(private val db: AppDatabase) {

    suspend fun obtenerOCrearPerfil(alias: String, avatarId: Int): PerfilEntity =
        db.perfilDao().obtener() ?: run {
            val nuevo = PerfilEntity(alias = alias, avatarId = avatarId)
            db.perfilDao().guardar(nuevo)
            nuevo
        }

    suspend fun listarActos() = db.actoDao().listarTodos()

    suspend fun listarPoemasDeActo(actoId: Int) = db.poemaDao().listarPorActo(actoId)

    suspend fun listarCartelera() = db.cartelDao().listarTodos()

    suspend fun listarMedallasGanadas(): Set<String> = db.medallaDao().listarGanadas().map { it.id }.toSet()

    suspend fun registrarIntento(
        poema: PoemaEntity,
        resultado: ResultadoIntento,
        hoy: LocalDate,
        esRepaso: Boolean = false
    ): ResultadoRegistroIntento {
        val intentosPrevios = db.intentoDeclamacionDao().listarPorPoema(poema.id)

        db.intentoDeclamacionDao().insertar(
            IntentoDeclamacionEntity(
                poemaId = poema.id,
                fechaEpochDay = hoy.toEpochDay(),
                volumenPromedio = resultado.volumenPromedio,
                volumenAdecuado = resultado.volumenAdecuado,
                entonacionAdecuada = resultado.entonacionAdecuada,
                silabasPorMinuto = resultado.silabasPorMinuto,
                ritmoAdecuado = resultado.ritmoAdecuado,
                pausasRespetadas = resultado.pausasRespetadas,
                pausasEsperadas = resultado.pausasEsperadas,
                aprobado = resultado.aprobado,
                contornoTonoCsv = resultado.contornoTono.joinToString(",") { it?.toString() ?: "" },
                esRepaso = esRepaso
            )
        )

        val eraDominado = poema.dominado
        val esPrimerIntentoAprobadoDeLaApp = resultado.aprobado &&
            db.poemaDao().listarTodos().none { it.dominado }

        if (resultado.aprobado && !eraDominado) {
            db.poemaDao().actualizar(poema.copy(dominado = true))
            db.cartelDao().guardar(
                CartelEntity(
                    poemaId = poema.id,
                    fechaObtencionEpochDay = hoy.toEpochDay(),
                    descripcionLogro = construirDescripcionLogro(resultado)
                )
            )
        }

        val poemasDelActo = db.poemaDao().listarPorActo(poema.actoId)
        val actoRecienCompletado = resultado.aprobado &&
            poemasDelActo.all { it.dominado || it.id == poema.id }
        val acto = db.actoDao().obtenerPorId(poema.actoId)
        val todosLosActos = db.actoDao().listarTodos()

        if (actoRecienCompletado && acto != null && !acto.completado) {
            db.actoDao().actualizar(acto.copy(completado = true))
            val siguienteOrden = MotorProgreso.actoSiguiente(acto.orden, todosLosActos.size, actoActualCompletado = true)
            if (siguienteOrden != null) {
                todosLosActos.find { it.orden == siguienteOrden }?.let { siguiente ->
                    db.actoDao().actualizar(siguiente.copy(desbloqueado = true))
                }
            }
        }

        val racha = db.rachaDao().obtener()
        val nuevaRacha = MotorProgreso.actualizarRacha(
            fechaUltimoIntento = racha?.let { LocalDate.ofEpochDay(it.ultimaFechaEpochDay) },
            fechaHoy = hoy,
            rachaActual = racha?.diasConsecutivos ?: 0
        )
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = nuevaRacha, ultimaFechaEpochDay = hoy.toEpochDay()))

        val esUltimoActo = acto != null && todosLosActos.isNotEmpty() && acto.orden == todosLosActos.maxOf { it.orden }
        val esRepeticionQueMejoraElResultado = eraDominado && resultado.aprobado
        val esPrimerRetoDeRepasoCompletado = esRepaso && resultado.aprobado &&
            intentosPrevios.none { it.esRepaso && it.aprobado }

        val medallasYaGanadas = listarMedallasGanadas()
        val medallasNuevas = MotorProgreso.evaluarMedallas(
            esPrimerIntentoAprobadoDeLaApp = esPrimerIntentoAprobadoDeLaApp,
            volumenAdecuadoEnTodoElPoema = resultado.volumenAdecuado,
            entonacionDentroDelContornoEn3Poemas = contarPoemasConUltimoIntentoQueCumple { it.entonacionAdecuada } >= 3,
            ritmoEstableEn3Poemas = contarPoemasConUltimoIntentoQueCumple { it.ritmoAdecuado } >= 3,
            todasLasPausasRespetadas = resultado.pausasEsperadas > 0 && resultado.pausasRespetadas == resultado.pausasEsperadas,
            actoRecienCompletado = actoRecienCompletado,
            esUltimoActo = esUltimoActo,
            esRepeticionQueMejoraElResultado = esRepeticionQueMejoraElResultado,
            esPrimerRetoDeRepasoCompletado = esPrimerRetoDeRepasoCompletado
        ).filterNot { it in medallasYaGanadas }.toSet()

        medallasNuevas.forEach { id ->
            db.medallaDao().guardar(MedallaEntity(id = id, fechaObtencionEpochDay = hoy.toEpochDay()))
        }

        return ResultadoRegistroIntento(
            medallasNuevas = medallasNuevas,
            actoDesbloqueadoSiguiente = actoRecienCompletado,
            cartelNuevo = resultado.aprobado && !eraDominado
        )
    }

    private suspend fun contarPoemasConUltimoIntentoQueCumple(
        predicado: (IntentoDeclamacionEntity) -> Boolean
    ): Int {
        var contador = 0
        for (poema in db.poemaDao().listarTodos()) {
            val ultimo = db.intentoDeclamacionDao().listarPorPoema(poema.id).firstOrNull()
            if (ultimo != null && predicado(ultimo)) contador++
        }
        return contador
    }

    private fun construirDescripcionLogro(resultado: ResultadoIntento): String = buildString {
        append(if (resultado.volumenAdecuado) "voz firme" else "voz en mejora")
        append(" y ")
        append(if (resultado.ritmoAdecuado) "ritmo sostenido" else "ritmo en mejora")
    }
}
