package pe.appmobile.elgrantelon.domain.engine

import pe.appmobile.elgrantelon.domain.model.ResultadoIntento

object MotorEvaluacionFuncion {

    fun evaluar(
        volumenPromedio: Float,
        rangoVolumenObjetivo: ClosedFloatingPointRange<Float>,
        contornoTono: List<Float?>,
        variacionMinimaTonoHz: Float,
        silabasPorMinuto: Int,
        rangoRitmoObjetivo: IntRange,
        pausasRespetadas: Int,
        pausasEsperadas: Int
    ): ResultadoIntento {
        val volumenAdecuado = volumenPromedio in rangoVolumenObjetivo
        val ritmoAdecuado = silabasPorMinuto in rangoRitmoObjetivo
        val minimoPausas = if (pausasEsperadas == 0) 0 else (pausasEsperadas + 1) / 2
        val pausasOk = pausasRespetadas >= minimoPausas

        val tonosValidos = contornoTono.filterNotNull()
        val entonacionAdecuada = tonosValidos.size >= 2 &&
            (tonosValidos.max() - tonosValidos.min()) >= variacionMinimaTonoHz

        return ResultadoIntento(
            volumenPromedio = volumenPromedio,
            volumenAdecuado = volumenAdecuado,
            contornoTono = contornoTono,
            entonacionAdecuada = entonacionAdecuada,
            silabasPorMinuto = silabasPorMinuto,
            ritmoAdecuado = ritmoAdecuado,
            pausasRespetadas = pausasRespetadas,
            pausasEsperadas = pausasEsperadas,
            aprobado = volumenAdecuado && ritmoAdecuado && pausasOk
        )
    }

    // Prioriza una sola pista accionable, en el mismo orden que exige
    // aprobado(): volumen, ritmo, pausas. Nunca solo "sigue intentando"
    // (seccion 5 del prompt maestro: siempre pista y explicacion, nunca
    // solo correcto/incorrecto).
    fun pistaPrincipal(resultado: ResultadoIntento): String = when {
        resultado.aprobado -> "¡Función lograda!"
        !resultado.volumenAdecuado -> "Prueba hablar un poco más fuerte, así Bemo brilla con más fuerza"
        !resultado.ritmoAdecuado -> "Prueba cambiar la velocidad: ni muy rápido ni muy lento"
        resultado.pausasRespetadas < resultado.pausasEsperadas ->
            "No olvides las pausas marcadas: dan tiempo a que Bemo se pose en silencio"
        else -> "Sigue ensayando, ya casi está"
    }
}
