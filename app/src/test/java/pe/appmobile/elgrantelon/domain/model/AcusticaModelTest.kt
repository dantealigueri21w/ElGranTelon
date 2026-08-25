package pe.appmobile.elgrantelon.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AcusticaModelTest {

    @Test
    fun `NivelVolumen tiene exactamente tres estados`() {
        assertEquals(3, NivelVolumen.entries.size)
    }

    @Test
    fun `LecturaVolumen guarda el rms y el nivel tal cual se construye`() {
        val lectura = LecturaVolumen(rms = 0.2f, nivel = NivelVolumen.ADECUADO)
        assertEquals(0.2f, lectura.rms, 0.0001f)
        assertEquals(NivelVolumen.ADECUADO, lectura.nivel)
    }

    @Test
    fun `ResultadoPausas guarda si se cumplieron las pausas`() {
        val resultado = ResultadoPausas(pausasDetectadas = 2, pausasEsperadas = 3, cumplidas = true)
        assertTrue(resultado.cumplidas)
    }
}
