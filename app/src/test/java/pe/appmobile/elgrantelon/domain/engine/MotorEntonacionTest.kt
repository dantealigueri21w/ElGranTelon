package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

class MotorEntonacionTest {

    private val sampleRate = 16000

    private fun generarTono(frecuenciaHz: Float, duracionMs: Int): FloatArray {
        val totalMuestras = sampleRate * duracionMs / 1000
        return FloatArray(totalMuestras) { i ->
            sin(2.0 * Math.PI * frecuenciaHz * i / sampleRate).toFloat()
        }
    }

    @Test
    fun `silencio total no reporta tono`() {
        val muestras = FloatArray(640) { 0f }
        assertNull(MotorEntonacion.detectarF0(muestras, sampleRate))
    }

    @Test
    fun `tono puro de 200 Hz se detecta dentro de tolerancia`() {
        val muestras = generarTono(200f, duracionMs = 40)
        val f0 = MotorEntonacion.detectarF0(muestras, sampleRate)
        assertNotNull(f0)
        assertTrue("esperado ~200Hz, fue $f0", abs(f0!! - 200f) <= 5f)
    }

    @Test
    fun `tono puro de 300 Hz se detecta dentro de tolerancia`() {
        val muestras = generarTono(300f, duracionMs = 40)
        val f0 = MotorEntonacion.detectarF0(muestras, sampleRate)
        assertNotNull(f0)
        assertTrue("esperado ~300Hz, fue $f0", abs(f0!! - 300f) <= 5f)
    }

    @Test
    fun `tono puro de 400 Hz cubre el rango agudo infantil`() {
        val muestras = generarTono(400f, duracionMs = 40)
        val f0 = MotorEntonacion.detectarF0(muestras, sampleRate)
        assertNotNull(f0)
        assertTrue("esperado ~400Hz, fue $f0", abs(f0!! - 400f) <= 5f)
    }

    @Test
    fun `ventana mas corta que el minimo necesario no revienta`() {
        val muestras = FloatArray(50) { 0.1f }
        assertNull(MotorEntonacion.detectarF0(muestras, sampleRate))
    }

    @Test
    fun `ruido aleatorio no lanza excepcion`() {
        val random = Random(42)
        val muestras = FloatArray(640) { random.nextFloat() * 2f - 1f }
        MotorEntonacion.detectarF0(muestras, sampleRate)
    }

    @Test
    fun `el contorno sigue una secuencia de tonos crecientes`() {
        val ventanas = listOf(
            generarTono(200f, duracionMs = 40),
            generarTono(300f, duracionMs = 40),
            generarTono(400f, duracionMs = 40)
        )
        val contorno = MotorEntonacion.calcularContorno(ventanas, sampleRate)
        assertEquals(3, contorno.size)
        assertTrue(contorno[0]!! < contorno[1]!!)
        assertTrue(contorno[1]!! < contorno[2]!!)
    }

    @Test
    fun `el contorno de una lista vacia es una lista vacia`() {
        assertEquals(0, MotorEntonacion.calcularContorno(emptyList(), sampleRate).size)
    }
}
