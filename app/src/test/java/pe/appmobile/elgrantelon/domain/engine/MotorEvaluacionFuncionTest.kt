package pe.appmobile.elgrantelon.domain.engine

import org.junit.Assert.assertTrue
import org.junit.Test

class MotorEvaluacionFuncionTest {

    private val rangoVolumen = 0.1f..0.4f
    private val rangoRitmo = 150..250
    private val variacionMinimaTono = 15f

    @Test
    fun `todo dentro de rango con pausas cumplidas y tono variado aprueba`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f, 240f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 2,
            pausasEsperadas = 3
        )
        assertTrue(resultado.aprobado)
        assertTrue(resultado.entonacionAdecuada)
    }

    @Test
    fun `volumen fuera de rango no aprueba`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.05f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 2,
            pausasEsperadas = 2
        )
        assertTrue(!resultado.aprobado)
    }

    @Test
    fun `ritmo fuera de rango no aprueba`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 400,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 2,
            pausasEsperadas = 2
        )
        assertTrue(!resultado.aprobado)
    }

    @Test
    fun `cumplir exactamente la mitad de las pausas aprueba`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 2,
            pausasEsperadas = 4
        )
        assertTrue(resultado.aprobado)
    }

    @Test
    fun `cumplir menos de la mitad de las pausas no aprueba`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 1,
            pausasEsperadas = 4
        )
        assertTrue(!resultado.aprobado)
    }

    @Test
    fun `la pista principal de un intento aprobado celebra el logro`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f, rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f), variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200, rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 2, pausasEsperadas = 2
        )
        assertTrue(MotorEvaluacionFuncion.pistaPrincipal(resultado).contains("lograda"))
    }

    @Test
    fun `la pista principal senala el volumen cuando es lo primero que falla`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.05f, rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f), variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 400, rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 0, pausasEsperadas = 4
        )
        assertTrue(!resultado.volumenAdecuado)
        assertTrue(MotorEvaluacionFuncion.pistaPrincipal(resultado).contains("fuerte"))
    }

    @Test
    fun `la pista principal senala el ritmo cuando el volumen ya esta bien`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f, rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f), variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 400, rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 0, pausasEsperadas = 4
        )
        assertTrue(MotorEvaluacionFuncion.pistaPrincipal(resultado).contains("velocidad"))
    }

    @Test
    fun `la pista principal senala las pausas cuando volumen y ritmo ya estan bien`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f, rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 220f), variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200, rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 0, pausasEsperadas = 4
        )
        assertTrue(MotorEvaluacionFuncion.pistaPrincipal(resultado).contains("pausa"))
    }

    @Test
    fun `sin pausas esperadas no penaliza, y un tono plano no cuenta como entonacion adecuada`() {
        val resultado = MotorEvaluacionFuncion.evaluar(
            volumenPromedio = 0.25f,
            rangoVolumenObjetivo = rangoVolumen,
            contornoTono = listOf(200f, 201f),
            variacionMinimaTonoHz = variacionMinimaTono,
            silabasPorMinuto = 200,
            rangoRitmoObjetivo = rangoRitmo,
            pausasRespetadas = 0,
            pausasEsperadas = 0
        )
        assertTrue(resultado.aprobado)
        assertTrue(!resultado.entonacionAdecuada)
    }
}
