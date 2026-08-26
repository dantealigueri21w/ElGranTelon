package pe.appmobile.elgrantelon.domain.engine

import pe.appmobile.elgrantelon.domain.model.ResultadoPausas
import pe.appmobile.elgrantelon.domain.model.SegmentoSilencio

object MotorPausas {

    fun detectarSilencios(
        energiasPorVentana: List<Float>,
        duracionVentanaMs: Long,
        umbralSilencio: Float,
        duracionMinimaPausaMs: Long
    ): List<SegmentoSilencio> {
        val segmentos = mutableListOf<SegmentoSilencio>()
        var inicioActual = -1

        fun cerrarSegmentoSiAplica(finExclusivo: Int) {
            if (inicioActual == -1) return
            val duracionMs = (finExclusivo - inicioActual) * duracionVentanaMs
            if (duracionMs >= duracionMinimaPausaMs) {
                segmentos.add(SegmentoSilencio(indiceInicio = inicioActual, duracionMs = duracionMs))
            }
            inicioActual = -1
        }

        for (i in energiasPorVentana.indices) {
            if (energiasPorVentana[i] <= umbralSilencio) {
                if (inicioActual == -1) inicioActual = i
            } else {
                cerrarSegmentoSiAplica(i)
            }
        }
        cerrarSegmentoSiAplica(energiasPorVentana.size)

        return segmentos
    }

    fun evaluarPausas(segmentosDetectados: List<SegmentoSilencio>, pausasEsperadas: Int): ResultadoPausas {
        if (pausasEsperadas == 0) {
            return ResultadoPausas(pausasDetectadas = segmentosDetectados.size, pausasEsperadas = 0, cumplidas = true)
        }
        val minimoParaCumplir = (pausasEsperadas + 1) / 2
        return ResultadoPausas(
            pausasDetectadas = segmentosDetectados.size,
            pausasEsperadas = pausasEsperadas,
            cumplidas = segmentosDetectados.size >= minimoParaCumplir
        )
    }
}
