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
}
