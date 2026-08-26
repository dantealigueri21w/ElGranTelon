package pe.appmobile.elgrantelon.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.appmobile.elgrantelon.ui.screens.AjustesScreen
import pe.appmobile.elgrantelon.ui.screens.AtrilScreen
import pe.appmobile.elgrantelon.ui.screens.CaeElTelonScreen
import pe.appmobile.elgrantelon.ui.screens.CamerinoScreen
import pe.appmobile.elgrantelon.ui.screens.CarteleraScreen
import pe.appmobile.elgrantelon.ui.screens.EscenarioScreen
import pe.appmobile.elgrantelon.ui.screens.FuncionDeRepasoScreen
import pe.appmobile.elgrantelon.ui.screens.ProgramaDeManoScreen
import pe.appmobile.elgrantelon.ui.screens.TeatroScreen
import pe.appmobile.elgrantelon.ui.screens.VitrinaScreen
import pe.appmobile.elgrantelon.ui.viewmodel.ElGranTelonViewModel

@Composable
fun ElGranTelonNavHost(viewModel: ElGranTelonViewModel = viewModel()) {
    val navController = rememberNavController()

    val perfil by viewModel.perfil.collectAsState()
    val actos by viewModel.actos.collectAsState()
    val poemasDelActo by viewModel.poemasDelActo.collectAsState()
    val poemaActual by viewModel.poemaActual.collectAsState()
    val medallasGanadas by viewModel.medallasGanadas.collectAsState()
    val cartelera by viewModel.cartelera.collectAsState()
    val resultadoUltimaFuncion by viewModel.resultadoUltimaFuncion.collectAsState()
    val medallasNuevas by viewModel.medallasNuevasUltimaFuncion.collectAsState()
    val grabando by viewModel.grabando.collectAsState()
    val lecturaEnVivo by viewModel.lecturaEnVivo.collectAsState()

    val destinoInicial = if (perfil == null) Rutas.CAMERINO else Rutas.TEATRO

    NavHost(navController = navController, startDestination = destinoInicial) {
        composable(Rutas.CAMERINO) {
            CamerinoScreen(
                avatares = viewModel.catalogoAvatares,
                onConfirmar = { alias, avatarId ->
                    viewModel.crearPerfil(alias, avatarId)
                    navController.navigate(Rutas.TEATRO) {
                        popUpTo(Rutas.CAMERINO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.TEATRO) {
            TeatroScreen(
                actos = actos,
                onAbrirActo = { actoId ->
                    viewModel.seleccionarActo(actoId)
                    navController.navigate(Rutas.programaDeMano(actoId))
                },
                onAbrirCamerino = { navController.navigate(Rutas.CAMERINO) },
                onAbrirCartelera = { navController.navigate(Rutas.CARTELERA) },
                onAbrirVitrina = { navController.navigate(Rutas.VITRINA) },
                onAbrirAjustes = { navController.navigate(Rutas.AJUSTES) }
            )
        }

        composable(
            Rutas.PROGRAMA_DE_MANO,
            arguments = listOf(navArgument("actoId") { type = NavType.IntType })
        ) {
            ProgramaDeManoScreen(
                poemas = poemasDelActo,
                onSeleccionarPoema = { poema ->
                    viewModel.seleccionarPoema(poema)
                    navController.navigate(Rutas.atril(poema.id))
                }
            )
        }

        composable(
            Rutas.ATRIL,
            arguments = listOf(navArgument("poemaId") { type = NavType.IntType })
        ) {
            poemaActual?.let { poema ->
                AtrilScreen(
                    poema = poema,
                    onEmpezarADeclamar = { navController.navigate(Rutas.ESCENARIO) }
                )
            }
        }

        composable(Rutas.ESCENARIO) {
            val context = LocalContext.current
            var tienePermiso by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                        PackageManager.PERMISSION_GRANTED
                )
            }
            val lanzadorPermiso = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { concedido -> tienePermiso = concedido }

            poemaActual?.let { poema ->
                EscenarioScreen(
                    poema = poema,
                    grabando = grabando,
                    lecturaEnVivo = lecturaEnVivo,
                    sinPermiso = !tienePermiso,
                    onEmpezar = {
                        if (tienePermiso) {
                            viewModel.iniciarDeclamacion()
                        } else {
                            lanzadorPermiso.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    onTerminar = {
                        viewModel.detenerDeclamacion()
                        navController.navigate(Rutas.CAE_EL_TELON)
                    }
                )
            }
        }

        composable(Rutas.CAE_EL_TELON) {
            resultadoUltimaFuncion?.let { resultado ->
                CaeElTelonScreen(
                    resultado = resultado,
                    medallasNuevas = viewModel.catalogoMedallas.filter { it.id in medallasNuevas },
                    onVolverAlTeatro = {
                        navController.navigate(Rutas.TEATRO) {
                            popUpTo(Rutas.TEATRO) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Rutas.CARTELERA) {
            CarteleraScreen(
                carteles = cartelera,
                poemasPorId = poemasDelActo.associateBy { it.id }
            )
        }

        composable(Rutas.VITRINA) {
            VitrinaScreen(
                catalogoMedallas = viewModel.catalogoMedallas,
                medallasGanadas = medallasGanadas
            )
        }

        composable(Rutas.FUNCION_DE_REPASO) {
            FuncionDeRepasoScreen(
                poemasDominados = poemasDelActo.filter { it.dominado },
                onRepasar = { poema ->
                    viewModel.seleccionarPoema(poema, esRepaso = true)
                    navController.navigate(Rutas.ESCENARIO)
                }
            )
        }

        composable(Rutas.AJUSTES) {
            AjustesScreen(alias = perfil?.alias)
        }
    }
}
