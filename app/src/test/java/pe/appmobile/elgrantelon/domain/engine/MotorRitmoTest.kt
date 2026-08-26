package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class MotorRitmoTest {

    private val umbral = 0.1f

    @Test
    fun `lista vacia da cero silabas y cero tasa`() {
        assertEquals(0, MotorRitmo.contarSilabas(emptyList(), 20L, umbral))
        assertEquals(0, MotorRitmo.silabasPorMinuto(emptyList(), 20L, umbral))
    }

    @Test
    fun `silencio total no cuenta silabas`() {
        val energias = List(20) { 0.02f }
        assertEquals(0, MotorRitmo.contarSilabas(energias, 20L, umbral))
    }

    @Test
    fun `un solo pico claro cuenta una silaba`() {
        val energias = listOf(0.02f, 0.02f, 0.5f, 0.02f, 0.02f)
        assertEquals(1, MotorRitmo.contarSilabas(energias, 20L, umbral))
    }

    @Test
    fun `varios picos separados cuentan cada uno`() {
        val energias = MutableList(40) { 0.02f }
        listOf(0, 10, 20, 30).forEach { energias[it] = 0.5f }
        assertEquals(4, MotorRitmo.contarSilabas(energias, 20L, umbral))
    }

    @Test
    fun `dos picos muy juntos cuentan como uno solo`() {
        val energias = MutableList(10) { 0.02f }
        energias[0] = 0.5f
        energias[3] = 0.5f
        assertEquals(1, MotorRitmo.contarSilabas(energias, 20L, umbral))
    }

    @Test
    fun `la tasa por minuto se calcula correctamente con un ejemplo conocido`() {
        val energias = MutableList(40) { 0.02f }
        listOf(0, 10, 20, 30).forEach { energias[it] = 0.5f }
        val tasa = MotorRitmo.silabasPorMinuto(energias, duracionVentanaMs = 20L, umbralVoz = umbral)
        assertEquals(300, tasa)
    }
}
