package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.data.entity.CartelEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.data.seed.SemillaAvatares
import pe.appmobile.elgrantelon.data.seed.SemillaMedallas
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento
import pe.appmobile.elgrantelon.ui.theme.ElGranTelonTheme

// Cada pantalla alcanzable se compone al menos una vez con datos reales de las
// semillas (seccion 10.1 del prompt maestro). Ni compileDebugKotlin ni
// testDebugUnitTest con tests de dominio/repositorio ejecutan una sola linea
// de Compose: un fallo que solo ocurre al medir o dibujar (por ejemplo un
// componente perezoso anidado dentro de un contenedor que ya hace scroll) es
// invisible para todo lo demas y solo lo atrapa este archivo.
@RunWith(RobolectricTestRunner::class)
class PantallasSinCrashTest {

    @get:Rule
    val compose = createComposeRule()

    private fun poemaDePrueba(id: Int = 1, actoId: Int = 1, dominado: Boolean = false) = PoemaEntity(
        id = id, actoId = actoId, titulo = "El primer aplauso", autor = "Original — El Gran Telón",
        texto = "Se abre el telón, se enciende la luz,\nmi voz todavía no sabe qué hacer.",
        marcasPausaCsv = "0", volumenMinimo = 0.08f, volumenMaximo = 0.30f,
        ritmoMinimoSilabasPorMinuto = 100, ritmoMaximoSilabasPorMinuto = 180, dominado = dominado
    )

    private fun actosDePrueba() = listOf(
        ActoEntity(id = 1, nombre = "Primeros pasos", orden = 1, desbloqueado = true, completado = false),
        ActoEntity(id = 2, nombre = "Subiendo al escenario", orden = 2, desbloqueado = false, completado = false),
        ActoEntity(id = 3, nombre = "Gran Función", orden = 3, desbloqueado = false, completado = false)
    )

    private fun resultadoDePrueba() = ResultadoIntento(
        volumenPromedio = 0.25f, volumenAdecuado = true,
        contornoTono = listOf(200f, 210f, null, 220f), entonacionAdecuada = true,
        silabasPorMinuto = 200, ritmoAdecuado = true,
        pausasRespetadas = 1, pausasEsperadas = 1, aprobado = true
    )

    @Test
    fun `CamerinoScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                CamerinoScreen(avatares = SemillaAvatares.avatares, onConfirmar = { _, _ -> })
            }
        }
    }

    @Test
    fun `TeatroScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                TeatroScreen(
                    actos = actosDePrueba(),
                    onAbrirActo = {}, onAbrirCamerino = {}, onAbrirCartelera = {},
                    onAbrirVitrina = {}, onAbrirAjustes = {}, onAbrirFuncionDeRepaso = {}
                )
            }
        }
    }

    @Test
    fun `TeatroScreen permite llegar a la Funcion de Repaso`() {
        // FuncionDeRepasoScreen ya existia en el NavHost pero no tenia
        // ningun punto de entrada real desde El Teatro: era inalcanzable
        // navegando, aunque los tests que la montan directo pasaran.
        var seAbrio = false
        compose.setContent {
            ElGranTelonTheme {
                TeatroScreen(
                    actos = actosDePrueba(),
                    onAbrirActo = {}, onAbrirCamerino = {}, onAbrirCartelera = {},
                    onAbrirVitrina = {}, onAbrirAjustes = {}, onAbrirFuncionDeRepaso = { seAbrio = true }
                )
            }
        }

        compose.onNodeWithText("Repasar").performClick()

        assertTrue(seAbrio)
    }

    @Test
    fun `TeatroScreen permite entrar a un Acto sin haber completado el anterior`() {
        // La seccion 5.1 del prompt maestro prohibe cualquier candado de
        // acceso: todo el contenido se toca y juega desde el primer minuto,
        // la progresion es guia, nunca bloqueo.
        var actoAbierto: Int? = null
        compose.setContent {
            ElGranTelonTheme {
                TeatroScreen(
                    actos = actosDePrueba(),
                    onAbrirActo = { actoAbierto = it }, onAbrirCamerino = {}, onAbrirCartelera = {},
                    onAbrirVitrina = {}, onAbrirAjustes = {}, onAbrirFuncionDeRepaso = {}
                )
            }
        }

        compose.onNodeWithText("Subiendo al escenario").performClick()

        assertEquals(2, actoAbierto)
    }

    @Test
    fun `ProgramaDeManoScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                ProgramaDeManoScreen(
                    poemas = listOf(poemaDePrueba(1), poemaDePrueba(2, dominado = true)),
                    onSeleccionarPoema = {}
                )
            }
        }
    }

    @Test
    fun `AtrilScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                AtrilScreen(poema = poemaDePrueba(), onEmpezarADeclamar = {})
            }
        }
    }

    @Test
    fun `EscenarioScreen no revienta la app sin permiso y sin grabar`() {
        compose.setContent {
            ElGranTelonTheme {
                EscenarioScreen(
                    poema = poemaDePrueba(), grabando = false, lecturaEnVivo = null,
                    sinPermiso = true, onEmpezar = {}, onTerminar = {}
                )
            }
        }
    }

    @Test
    fun `EscenarioScreen permite tocar Empezar sin permiso para poder pedirlo`() {
        // Sin esto, el usuario que nunca concedio el permiso ve el boton
        // deshabilitado para siempre: no hay forma de disparar el dialogo
        // de permiso del sistema desde dentro de la app.
        var seToco = false
        compose.setContent {
            ElGranTelonTheme {
                EscenarioScreen(
                    poema = poemaDePrueba(), grabando = false, lecturaEnVivo = null,
                    sinPermiso = true, onEmpezar = { seToco = true }, onTerminar = {}
                )
            }
        }

        compose.onNodeWithText("Empezar").performClick()

        assertTrue(seToco)
    }

    @Test
    fun `EscenarioScreen no revienta la app con permiso y grabando`() {
        compose.setContent {
            ElGranTelonTheme {
                EscenarioScreen(
                    poema = poemaDePrueba(), grabando = true, lecturaEnVivo = null,
                    sinPermiso = false, onEmpezar = {}, onTerminar = {}
                )
            }
        }
    }

    @Test
    fun `CaeElTelonScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                CaeElTelonScreen(
                    resultado = resultadoDePrueba(),
                    medallasNuevas = SemillaMedallas.catalogo.take(1),
                    onVolverAlTeatro = {}, onReintentar = {}
                )
            }
        }
    }

    @Test
    fun `CaeElTelonScreen ofrece intentar de nuevo cuando no se aprueba`() {
        // Seccion 5 del prompt maestro: nunca solo "Correcto/Incorrecto",
        // siempre poder reintentar sin humillacion.
        var seReintento = false
        compose.setContent {
            ElGranTelonTheme {
                CaeElTelonScreen(
                    resultado = resultadoDePrueba().copy(
                        aprobado = false, volumenAdecuado = false, ritmoAdecuado = true
                    ),
                    medallasNuevas = emptyList(),
                    onVolverAlTeatro = {}, onReintentar = { seReintento = true }
                )
            }
        }

        compose.onNodeWithText("Intentar de nuevo").performScrollTo().performClick()

        assertTrue(seReintento)
    }

    @Test
    fun `CarteleraScreen no revienta la app vacia`() {
        compose.setContent {
            ElGranTelonTheme {
                CarteleraScreen(carteles = emptyList(), poemasPorId = emptyMap())
            }
        }
    }

    @Test
    fun `CarteleraScreen no revienta la app con datos`() {
        val poema = poemaDePrueba()
        compose.setContent {
            ElGranTelonTheme {
                CarteleraScreen(
                    carteles = listOf(CartelEntity(poemaId = poema.id, fechaObtencionEpochDay = 1L, descripcionLogro = "voz firme")),
                    poemasPorId = mapOf(poema.id to poema)
                )
            }
        }
    }

    @Test
    fun `VitrinaScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                VitrinaScreen(
                    catalogoMedallas = SemillaMedallas.catalogo,
                    medallasGanadas = setOf("primera_funcion")
                )
            }
        }
    }

    @Test
    fun `FuncionDeRepasoScreen no revienta la app vacia`() {
        compose.setContent {
            ElGranTelonTheme {
                FuncionDeRepasoScreen(poemasDominados = emptyList(), onRepasar = {})
            }
        }
    }

    @Test
    fun `FuncionDeRepasoScreen no revienta la app con datos`() {
        compose.setContent {
            ElGranTelonTheme {
                FuncionDeRepasoScreen(poemasDominados = listOf(poemaDePrueba(dominado = true)), onRepasar = {})
            }
        }
    }

    @Test
    fun `AjustesScreen no revienta la app`() {
        compose.setContent {
            ElGranTelonTheme {
                AjustesScreen(alias = "Actor misterioso")
            }
        }
    }
}
