package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class MotorProgresoTest {

    @Test
    fun `completar un acto desbloquea el siguiente`() {
        assertEquals(2, MotorProgreso.actoSiguiente(actoActualId = 1, totalActos = 3, actoActualCompletado = true))
    }

    @Test
    fun `completar el ultimo acto no desbloquea nada`() {
        assertNull(MotorProgreso.actoSiguiente(actoActualId = 3, totalActos = 3, actoActualCompletado = true))
    }

    @Test
    fun `no completar el acto no desbloquea nada`() {
        assertNull(MotorProgreso.actoSiguiente(actoActualId = 1, totalActos = 3, actoActualCompletado = false))
    }

    @Test
    fun `la racha no crece dos veces el mismo dia`() {
        val hoy = LocalDate.of(2026, 8, 25)
        assertEquals(3, MotorProgreso.actualizarRacha(hoy, hoy, rachaActual = 3))
    }

    @Test
    fun `la racha crece en un dia consecutivo y se reinicia si se salta un dia`() {
        val hoy = LocalDate.of(2026, 8, 25)
        assertEquals(1, MotorProgreso.actualizarRacha(null, hoy, rachaActual = 0))
        assertEquals(4, MotorProgreso.actualizarRacha(hoy.minusDays(1), hoy, rachaActual = 3))
        assertEquals(1, MotorProgreso.actualizarRacha(hoy.minusDays(3), hoy, rachaActual = 5))
    }

    @Test
    fun `evaluarMedallas otorga solo las medallas que corresponden`() {
        val soloPrimeraFuncion = MotorProgreso.evaluarMedallas(
            esPrimerIntentoAprobadoDeLaApp = true,
            volumenAdecuadoEnTodoElPoema = false,
            entonacionDentroDelContornoEn3Poemas = false,
            ritmoEstableEn3Poemas = false,
            todasLasPausasRespetadas = false,
            actoRecienCompletado = false,
            esUltimoActo = false,
            esRepeticionQueMejoraElResultado = false,
            esPrimerRetoDeRepasoCompletado = false
        )
        assertEquals(setOf("primera_funcion"), soloPrimeraFuncion)

        val variasALaVez = MotorProgreso.evaluarMedallas(
            esPrimerIntentoAprobadoDeLaApp = false,
            volumenAdecuadoEnTodoElPoema = true,
            entonacionDentroDelContornoEn3Poemas = true,
            ritmoEstableEn3Poemas = false,
            todasLasPausasRespetadas = false,
            actoRecienCompletado = true,
            esUltimoActo = true,
            esRepeticionQueMejoraElResultado = false,
            esPrimerRetoDeRepasoCompletado = false
        )
        assertEquals(setOf("voz_de_trueno", "buen_oido", "funcion_llena", "telon_de_gala"), variasALaVez)
    }
}
