package pe.appmobile.elgrantelon.domain.engine

import kotlin.math.roundToInt

object MotorRitmo {

    private const val DISTANCIA_MINIMA_ENTRE_SILABAS_MS = 150L

    fun contarSilabas(energiasPorVentana: List<Float>, duracionVentanaMs: Long, umbralVoz: Float): Int {
        if (energiasPorVentana.isEmpty()) return 0
        val distanciaMinimaVentanas = (DISTANCIA_MINIMA_ENTRE_SILABAS_MS / duracionVentanaMs)
            .toInt()
            .coerceAtLeast(1)

        var conteo = 0
        var ultimoPico = -distanciaMinimaVentanas
        for (i in energiasPorVentana.indices) {
            val actual = energiasPorVentana[i]
            val esPico = actual > umbralVoz &&
                (i == 0 || actual >= energiasPorVentana[i - 1]) &&
                (i == energiasPorVentana.lastIndex || actual >= energiasPorVentana[i + 1])
            if (esPico && (i - ultimoPico) >= distanciaMinimaVentanas) {
                conteo++
                ultimoPico = i
            }
        }
        return conteo
    }

    fun silabasPorMinuto(energiasPorVentana: List<Float>, duracionVentanaMs: Long, umbralVoz: Float): Int {
        if (energiasPorVentana.isEmpty()) return 0
        val conteo = contarSilabas(energiasPorVentana, duracionVentanaMs, umbralVoz)
        val duracionTotalMinutos = (energiasPorVentana.size * duracionVentanaMs) / 60000f
        if (duracionTotalMinutos <= 0f) return 0
        return (conteo / duracionTotalMinutos).roundToInt()
    }
}
