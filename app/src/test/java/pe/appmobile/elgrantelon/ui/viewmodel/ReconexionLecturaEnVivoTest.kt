package pe.appmobile.elgrantelon.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// Aisla el patron real que tenia ElGranTelonViewModel.lecturaEnVivo antes de
// esta correccion: un StateFlow que solo existe mientras hay una captura
// activa, expuesto por un getter que devuelve una instancia distinta cada vez
// que arranca una nueva captura. Root cause del bug real "Bemo no reacciona
// aunque se haya hablado": collectAsState() ya esta suscrito al StateFlow
// "fantasma" (capturador == null) desde la primera composicion, y al cambiar
// de instancia no se reconecta — la UI se queda sorda para siempre.
private class ColaboradorConFlowFantasma {
    private val _activo = MutableStateFlow(false)
    val activo: StateFlow<Boolean> = _activo.asStateFlow()

    private var fuente: MutableStateFlow<Int?>? = null
    val valorEnVivo: StateFlow<Int?>
        get() = fuente?.asStateFlow() ?: MutableStateFlow<Int?>(null).asStateFlow()

    fun iniciar() {
        val nuevaFuente = MutableStateFlow<Int?>(0)
        fuente = nuevaFuente
        _activo.value = true
        nuevaFuente.value = 42
    }
}

// El patron corregido, aplicado en ElGranTelonViewModel: un unico StateFlow
// estable durante toda la vida del colaborador. Solo cambia su valor, nunca
// su identidad, asi que collectAsState() no necesita reconectarse a nada.
private class ColaboradorConFlowEstable {
    private val _activo = MutableStateFlow(false)
    val activo: StateFlow<Boolean> = _activo.asStateFlow()

    private val _valorEnVivo = MutableStateFlow<Int?>(null)
    val valorEnVivo: StateFlow<Int?> = _valorEnVivo.asStateFlow()

    fun iniciar() {
        _activo.value = true
        _valorEnVivo.value = 42
    }
}

@RunWith(RobolectricTestRunner::class)
class ReconexionLecturaEnVivoTest {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `un StateFlow fantasma que cambia de instancia deja a la UI sorda`() {
        val colaborador = ColaboradorConFlowFantasma()
        var ultimoValorVisto: Int? = -1

        compose.setContent {
            val activo by colaborador.activo.collectAsState()
            val valor by colaborador.valorEnVivo.collectAsState()
            ultimoValorVisto = valor
        }

        compose.runOnIdle { colaborador.iniciar() }
        compose.waitForIdle()

        assertEquals(null, ultimoValorVisto)
    }

    @Test
    fun `un StateFlow estable propaga el valor real sin reconexion`() {
        val colaborador = ColaboradorConFlowEstable()
        var ultimoValorVisto: Int? = -1

        compose.setContent {
            val activo by colaborador.activo.collectAsState()
            val valor by colaborador.valorEnVivo.collectAsState()
            ultimoValorVisto = valor
        }

        compose.runOnIdle { colaborador.iniciar() }
        compose.waitForIdle()

        assertEquals(42, ultimoValorVisto)
    }
}
