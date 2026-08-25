package pe.appmobile.elgrantelon.domain.engine

import kotlin.math.sqrt
import pe.appmobile.elgrantelon.domain.model.LecturaVolumen
import pe.appmobile.elgrantelon.domain.model.NivelVolumen

/** Asume amplitud normalizada de -1.0 a 1.0 en muestras y rangoObjetivo (como AudioRecord en modo READ_FLOAT); PCM de 16 bits sin normalizar clasificaria todo como ALTO para siempre. */
object MotorVolumen {

    fun calcularRms(muestras: FloatArray): Float {
        if (muestras.isEmpty()) return 0f
        var sumaCuadrados = 0.0
        for (muestra in muestras) {
            sumaCuadrados += muestra.toDouble() * muestra.toDouble()
        }
        return sqrt(sumaCuadrados / muestras.size).toFloat()
    }

    fun clasificar(rms: Float, rangoObjetivo: ClosedFloatingPointRange<Float>): NivelVolumen = when {
        rms < rangoObjetivo.start -> NivelVolumen.BAJO
        rms > rangoObjetivo.endInclusive -> NivelVolumen.ALTO
        else -> NivelVolumen.ADECUADO
    }

    fun leer(muestras: FloatArray, rangoObjetivo: ClosedFloatingPointRange<Float>): LecturaVolumen {
        val rms = calcularRms(muestras)
        return LecturaVolumen(rms = rms, nivel = clasificar(rms, rangoObjetivo))
    }
}
