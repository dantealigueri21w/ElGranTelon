package pe.appmobile.elgrantelon.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.appmobile.elgrantelon.domain.engine.LecturaEnVivo
import pe.appmobile.elgrantelon.domain.engine.MotorCaptura
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento

// Unico punto del proyecto que toca android.media.AudioRecord. Lee el
// microfono en ventanas de 40 ms y se las pasa a MotorCaptura (probado con
// audio sintetico) — esta clase en si no tiene tests unitarios porque no hay
// forma de simular un microfono real en la JVM; su unica responsabilidad es
// leer bytes reales y delegar todo el calculo al motor ya verificado.
class CapturadorVoz(
    private val context: Context,
    private val rangoVolumenObjetivo: ClosedFloatingPointRange<Float>,
    private val umbralSilencio: Float = UMBRAL_SILENCIO_DEFECTO
) {
    private val motor = MotorCaptura(
        sampleRate = SAMPLE_RATE,
        duracionVentanaMs = DURACION_VENTANA_MS,
        rangoVolumenObjetivo = rangoVolumenObjetivo,
        umbralSilencio = umbralSilencio,
        ventanasPorPublicacion = VENTANAS_POR_PUBLICACION
    )

    private val _lecturaEnVivo = MutableStateFlow<LecturaEnVivo?>(null)
    val lecturaEnVivo: StateFlow<LecturaEnVivo?> = _lecturaEnVivo

    private var audioRecord: AudioRecord? = null
    private var trabajoGrabacion: Job? = null
    private var grabando = false
    private val alcance = CoroutineScope(Dispatchers.Default)

    fun tienePermiso(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED

    fun iniciar(): Boolean {
        if (audioRecord != null) return false
        // Chequeo en linea, no a traves de tienePermiso(): el analisis de lint
        // para MissingPermission solo reconoce el patron ContextCompat.check...
        // == PERMISSION_GRANTED justo antes de la llamada, no a traves de un
        // metodo auxiliar.
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        motor.reiniciar()
        _lecturaEnVivo.value = null

        val tamanoMinimo = AudioRecord.getMinBufferSize(
            SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_FLOAT
        )
        if (tamanoMinimo <= 0) return false
        val tamanoBuffer = tamanoMinimo.coerceAtLeast(MUESTRAS_POR_VENTANA * 4)

        val grabador = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,
            tamanoBuffer
        )
        if (grabador.state != AudioRecord.STATE_INITIALIZED) {
            grabador.release()
            return false
        }

        audioRecord = grabador
        grabando = true
        grabador.startRecording()

        trabajoGrabacion = alcance.launch {
            val buffer = FloatArray(MUESTRAS_POR_VENTANA)
            while (grabando) {
                val leidas = grabador.read(buffer, 0, buffer.size, AudioRecord.READ_BLOCKING)
                if (!grabando) break
                if (leidas <= 0) continue
                val ventana = if (leidas == buffer.size) buffer.copyOf() else buffer.copyOf(leidas)
                val lectura = motor.procesarVentana(ventana)
                if (lectura != null) _lecturaEnVivo.value = lectura
            }
        }
        return true
    }

    fun detener(
        rangoRitmoObjetivo: IntRange,
        variacionMinimaTonoHz: Float,
        duracionMinimaPausaMs: Long,
        pausasEsperadas: Int
    ): ResultadoIntento {
        grabando = false
        audioRecord?.stop()
        trabajoGrabacion?.cancel()
        trabajoGrabacion = null
        audioRecord?.release()
        audioRecord = null

        return motor.finalizar(
            rangoRitmoObjetivo = rangoRitmoObjetivo,
            variacionMinimaTonoHz = variacionMinimaTonoHz,
            duracionMinimaPausaMs = duracionMinimaPausaMs,
            pausasEsperadas = pausasEsperadas
        )
    }

    companion object {
        const val SAMPLE_RATE = 16000
        private const val DURACION_VENTANA_MS = 40L
        private const val MUESTRAS_POR_VENTANA = (SAMPLE_RATE * DURACION_VENTANA_MS / 1000).toInt()
        private const val VENTANAS_POR_PUBLICACION = 4 // 4 x 40ms = 160ms, dentro del rango 150-200ms decidido
        const val UMBRAL_SILENCIO_DEFECTO = 0.05f
    }
}
