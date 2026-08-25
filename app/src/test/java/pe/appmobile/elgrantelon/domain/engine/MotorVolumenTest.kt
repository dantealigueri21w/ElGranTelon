package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test
import pe.appmobile.elgrantelon.domain.model.NivelVolumen

class MotorVolumenTest {

    private val rangoObjetivo = 0.1f..0.4f

    @Test
    fun `silencio total da RMS cero`() {
        val muestras = FloatArray(100) { 0f }
        assertEquals(0f, MotorVolumen.calcularRms(muestras), 0.0001f)
    }

    @Test
    fun `amplitud constante conocida da el RMS esperado`() {
        val muestras = FloatArray(200) { 0.5f }
        assertEquals(0.5f, MotorVolumen.calcularRms(muestras), 0.001f)
    }

    @Test
    fun `ventana vacia no revienta y da RMS cero`() {
        assertEquals(0f, MotorVolumen.calcularRms(FloatArray(0)), 0.0001f)
    }

    @Test
    fun `rms dentro del rango objetivo clasifica ADECUADO`() {
        val muestras = FloatArray(200) { 0.25f }
        val lectura = MotorVolumen.leer(muestras, rangoObjetivo)
        assertEquals(NivelVolumen.ADECUADO, lectura.nivel)
    }

    @Test
    fun `rms por debajo del rango clasifica BAJO`() {
        val muestras = FloatArray(200) { 0.02f }
        val lectura = MotorVolumen.leer(muestras, rangoObjetivo)
        assertEquals(NivelVolumen.BAJO, lectura.nivel)
    }

    @Test
    fun `rms por encima del rango clasifica ALTO`() {
        val muestras = FloatArray(200) { 0.7f }
        val lectura = MotorVolumen.leer(muestras, rangoObjetivo)
        assertEquals(NivelVolumen.ALTO, lectura.nivel)
    }
}
