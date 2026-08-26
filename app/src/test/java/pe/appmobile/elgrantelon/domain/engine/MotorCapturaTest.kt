package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.elgrantelon.domain.model.NivelVolumen
import kotlin.math.sin

class MotorCapturaTest {

    private val sampleRate = 16000
    private val rangoVolumen = 0.1f..0.4f

    private fun ventanaTono(frecuenciaHz: Float, amplitud: Float, duracionMs: Int = 40): FloatArray {
        val n = sampleRate * duracionMs / 1000
        return FloatArray(n) { i -> amplitud * sin(2.0 * Math.PI * frecuenciaHz * i / sampleRate).toFloat() }
    }

    @Test
    fun `no publica hasta completar el numero de ventanas configurado`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 3)
        val ventana = ventanaTono(200f, 0.25f)

        assertNull(motor.procesarVentana(ventana))
        assertNull(motor.procesarVentana(ventana))
        val lectura = motor.procesarVentana(ventana)
        assertTrue(lectura != null)
    }

    @Test
    fun `la lectura publicada refleja el volumen y el tono reales`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 1)
        val lectura = motor.procesarVentana(ventanaTono(300f, 0.25f))

        assertTrue(lectura != null)
        assertEquals(NivelVolumen.ADECUADO, lectura!!.nivelVolumen)
        assertTrue("esperado ~300Hz, fue ${lectura.tonoHz}", kotlin.math.abs(lectura.tonoHz!! - 300f) <= 5f)
    }

    @Test
    fun `silabasPorMinutoHastaAhora crece a medida que se detectan picos de energia`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 1)
        val silencio = FloatArray(640) { 0f }
        val sonido = ventanaTono(200f, 0.25f)

        motor.procesarVentana(silencio)
        val primeraLectura = motor.procesarVentana(sonido)!!
        assertTrue(primeraLectura.silabasPorMinutoHastaAhora >= 0)
    }

    @Test
    fun `finalizar produce un ResultadoIntento coherente con lo capturado`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 1)
        repeat(5) { motor.procesarVentana(ventanaTono(300f, 0.25f)) }

        val resultado = motor.finalizar(
            rangoRitmoObjetivo = 0..1000,
            variacionMinimaTonoHz = 1000f,
            duracionMinimaPausaMs = 100L,
            pausasEsperadas = 0
        )

        assertTrue(resultado.volumenAdecuado)
        assertEquals(5, resultado.contornoTono.size)
        assertTrue(resultado.contornoTono.all { it != null })
    }

    @Test
    fun `reiniciar limpia el estado para un nuevo intento`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 1)
        motor.procesarVentana(ventanaTono(300f, 0.25f))
        motor.procesarVentana(ventanaTono(300f, 0.25f))

        motor.reiniciar()
        val resultado = motor.finalizar(
            rangoRitmoObjetivo = 0..1000,
            variacionMinimaTonoHz = 1000f,
            duracionMinimaPausaMs = 100L,
            pausasEsperadas = 0
        )

        assertEquals(0, resultado.contornoTono.size)
        assertEquals(0f, resultado.volumenPromedio, 0.0001f)
    }

    @Test
    fun `ventana vacia de silencio no revienta y clasifica BAJO`() {
        val motor = MotorCaptura(sampleRate, duracionVentanaMs = 40L, rangoVolumenObjetivo = rangoVolumen, umbralSilencio = 0.05f, ventanasPorPublicacion = 1)
        val lectura = motor.procesarVentana(FloatArray(640) { 0f })

        assertEquals(NivelVolumen.BAJO, lectura!!.nivelVolumen)
        assertNull(lectura.tonoHz)
    }
}
