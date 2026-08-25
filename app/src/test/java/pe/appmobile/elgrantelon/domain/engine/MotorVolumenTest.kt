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
    fun `rms de una señal alternante se calcula correctamente, no como el promedio absoluto`() {
        val muestras = floatArrayOf(1f, -1f, 1f, -1f)
        assertEquals(1.0f, MotorVolumen.calcularRms(muestras), 0.0001f)
    }

    @Test
    fun `rms de una señal de magnitud variable distingue el cuadrado del valor absoluto`() {
        val muestras = floatArrayOf(1f, -3f)
        // cuadrados = [1, 9], media = 5, raiz = sqrt(5) ~= 2.236
        // (una implementacion con el bug abs-en-vez-de-cuadrado daria (1+3)/2 = 2.0, un valor distinto)
        assertEquals(2.236f, MotorVolumen.calcularRms(muestras), 0.001f)
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

    @Test
    fun `rms exactamente en el limite inferior del rango clasifica ADECUADO`() {
        assertEquals(NivelVolumen.ADECUADO, MotorVolumen.clasificar(0.1f, rangoObjetivo))
    }

    @Test
    fun `rms exactamente en el limite superior del rango clasifica ADECUADO`() {
        assertEquals(NivelVolumen.ADECUADO, MotorVolumen.clasificar(0.4f, rangoObjetivo))
    }
}
