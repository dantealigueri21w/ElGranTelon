package pe.appmobile.elgrantelon.domain.engine

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object MotorProgreso {

    fun actoSiguiente(actoActualId: Int, totalActos: Int, actoActualCompletado: Boolean): Int? {
        if (!actoActualCompletado) return null
        val siguiente = actoActualId + 1
        return if (siguiente <= totalActos) siguiente else null
    }

    fun actualizarRacha(fechaUltimoIntento: LocalDate?, fechaHoy: LocalDate, rachaActual: Int): Int {
        if (fechaUltimoIntento == null) return 1
        val diasTranscurridos = ChronoUnit.DAYS.between(fechaUltimoIntento, fechaHoy)
        return when {
            diasTranscurridos == 0L -> rachaActual.coerceAtLeast(1)
            diasTranscurridos == 1L -> rachaActual + 1
            else -> 1
        }
    }

    fun evaluarMedallas(
        esPrimerIntentoAprobadoDeLaApp: Boolean,
        volumenAdecuadoEnTodoElPoema: Boolean,
        entonacionDentroDelContornoEn3Poemas: Boolean,
        ritmoEstableEn3Poemas: Boolean,
        todasLasPausasRespetadas: Boolean,
        actoRecienCompletado: Boolean,
        esUltimoActo: Boolean,
        esRepeticionQueMejoraElResultado: Boolean,
        esPrimerRetoDeRepasoCompletado: Boolean
    ): Set<String> {
        val medallas = mutableSetOf<String>()
        if (esPrimerIntentoAprobadoDeLaApp) medallas += "primera_funcion"
        if (volumenAdecuadoEnTodoElPoema) medallas += "voz_de_trueno"
        if (entonacionDentroDelContornoEn3Poemas) medallas += "buen_oido"
        if (ritmoEstableEn3Poemas) medallas += "paso_de_actor"
        if (todasLasPausasRespetadas) medallas += "silencio_de_oro"
        if (actoRecienCompletado) medallas += "funcion_llena"
        if (esRepeticionQueMejoraElResultado) medallas += "bis"
        if (esPrimerRetoDeRepasoCompletado) medallas += "voz_propia"
        if (actoRecienCompletado && esUltimoActo) medallas += "telon_de_gala"
        return medallas
    }
}
