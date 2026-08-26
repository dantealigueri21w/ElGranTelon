package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.elgrantelon.domain.model.SegmentoSilencio

class MotorPausasTest {

    private val umbral = 0.05f

    @Test
    fun `sin silencios no detecta segmentos`() {
        val energias = List(10) { 0.5f }
        assertTrue(MotorPausas.detectarSilencios(energias, 20L, umbral, 100L).isEmpty())
    }

    @Test
    fun `un silencio claro se detecta con su duracion correcta`() {
        val energias = listOf(0.5f, 0.5f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.5f, 0.5f)
        val segmentos = MotorPausas.detectarSilencios(energias, duracionVentanaMs = 20L, umbral, 100L)
        assertEquals(1, segmentos.size)
        assertEquals(2, segmentos[0].indiceInicio)
        assertEquals(100L, segmentos[0].duracionMs)
    }

    @Test
    fun `un silencio corto por debajo del minimo no se cuenta`() {
        val energias = listOf(0.5f, 0.01f, 0.01f, 0.5f)
        val segmentos = MotorPausas.detectarSilencios(energias, duracionVentanaMs = 20L, umbral, 100L)
        assertTrue(segmentos.isEmpty())
    }

    @Test
    fun `varios silencios validos se cuentan todos`() {
        val energias = listOf(
            0.5f, 0.5f,
            0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
            0.5f, 0.5f,
            0.01f, 0.01f, 0.01f, 0.01f, 0.01f, 0.01f,
            0.5f
        )
        val segmentos = MotorPausas.detectarSilencios(energias, duracionVentanaMs = 20L, umbral, 100L)
        assertEquals(2, segmentos.size)
    }

    @Test
    fun `evaluarPausas cumple cuando se detecta al menos la mitad de las esperadas`() {
        val tresSegmentos = listOf(
            SegmentoSilencio(0, 100L), SegmentoSilencio(10, 100L), SegmentoSilencio(20, 100L)
        )
        assertTrue(MotorPausas.evaluarPausas(tresSegmentos, pausasEsperadas = 3).cumplidas)

        val unSegmento = listOf(SegmentoSilencio(0, 100L))
        assertTrue(!MotorPausas.evaluarPausas(unSegmento, pausasEsperadas = 3).cumplidas)
    }
}
