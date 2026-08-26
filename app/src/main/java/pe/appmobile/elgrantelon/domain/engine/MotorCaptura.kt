package pe.appmobile.elgrantelon.domain.engine

import pe.appmobile.elgrantelon.domain.model.NivelVolumen
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento

data class LecturaEnVivo(
    val nivelVolumen: NivelVolumen,
    val tonoHz: Float?,
    val silabasPorMinutoHastaAhora: Int
)

// Orquesta los cuatro motores puros durante una declamación en curso. No toca
// AudioRecord ni ninguna API de Android: recibe ventanas de muestras ya
// capturadas (misma forma que en los tests de los motores individuales) y
// publica una LecturaEnVivo cada `ventanasPorPublicacion` llamadas — la
// actualizacion relajada (~150-200ms) decidida para Bemo, en vez de una lectura
// por cada ventana de ~40ms. La captura real con el microfono vive en
// pe.appmobile.elgrantelon.audio.CapturadorVoz, que llama a esta clase.
class MotorCaptura(
    private val sampleRate: Int,
    private val duracionVentanaMs: Long,
    private val rangoVolumenObjetivo: ClosedFloatingPointRange<Float>,
    private val umbralSilencio: Float,
    private val ventanasPorPublicacion: Int
) {
    private val energias = mutableListOf<Float>()
    private val contornoTono = mutableListOf<Float?>()
    private var contadorVentanas = 0

    fun procesarVentana(muestras: FloatArray): LecturaEnVivo? {
        val rms = MotorVolumen.calcularRms(muestras)
        val tono = MotorEntonacion.detectarF0(muestras, sampleRate)
        energias.add(rms)
        contornoTono.add(tono)
        contadorVentanas++

        if (contadorVentanas % ventanasPorPublicacion != 0) return null

        return LecturaEnVivo(
            nivelVolumen = MotorVolumen.clasificar(rms, rangoVolumenObjetivo),
            tonoHz = tono,
            silabasPorMinutoHastaAhora = MotorRitmo.silabasPorMinuto(energias, duracionVentanaMs, umbralSilencio)
        )
    }

    fun finalizar(
        rangoRitmoObjetivo: IntRange,
        variacionMinimaTonoHz: Float,
        duracionMinimaPausaMs: Long,
        pausasEsperadas: Int
    ): ResultadoIntento {
        val volumenPromedio = if (energias.isEmpty()) 0f else energias.average().toFloat()
        val silabasPorMinuto = MotorRitmo.silabasPorMinuto(energias, duracionVentanaMs, umbralSilencio)
        val segmentos = MotorPausas.detectarSilencios(energias, duracionVentanaMs, umbralSilencio, duracionMinimaPausaMs)

        return MotorEvaluacionFuncion.evaluar(
            volumenPromedio = volumenPromedio,
            rangoVolumenObjetivo = rangoVolumenObjetivo,
            contornoTono = contornoTono.toList(),
            variacionMinimaTonoHz = variacionMinimaTonoHz,
            silabasPorMinuto = silabasPorMinuto,
            rangoRitmoObjetivo = rangoRitmoObjetivo,
            pausasRespetadas = segmentos.size,
            pausasEsperadas = pausasEsperadas
        )
    }

    fun reiniciar() {
        energias.clear()
        contornoTono.clear()
        contadorVentanas = 0
    }
}
