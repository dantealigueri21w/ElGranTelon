package pe.appmobile.elgrantelon.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.elgrantelon.audio.CapturadorVoz
import pe.appmobile.elgrantelon.data.AppDatabase
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.data.entity.CartelEntity
import pe.appmobile.elgrantelon.data.entity.PerfilEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.data.repository.ElGranTelonRepository
import pe.appmobile.elgrantelon.data.repository.ResultadoRegistroIntento
import pe.appmobile.elgrantelon.data.seed.SemillaAvatares
import pe.appmobile.elgrantelon.data.seed.SemillaFrasesBemo
import pe.appmobile.elgrantelon.data.seed.SemillaMedallas
import pe.appmobile.elgrantelon.domain.engine.LecturaEnVivo
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento
import java.time.LocalDate

class ElGranTelonViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.obtener(application)
    private val repositorio = ElGranTelonRepository(db)

    private val _perfil = MutableStateFlow<PerfilEntity?>(null)
    val perfil: StateFlow<PerfilEntity?> = _perfil.asStateFlow()

    private val _actos = MutableStateFlow<List<ActoEntity>>(emptyList())
    val actos: StateFlow<List<ActoEntity>> = _actos.asStateFlow()

    private val _poemasDelActo = MutableStateFlow<List<PoemaEntity>>(emptyList())
    val poemasDelActo: StateFlow<List<PoemaEntity>> = _poemasDelActo.asStateFlow()

    private val _poemaActual = MutableStateFlow<PoemaEntity?>(null)
    val poemaActual: StateFlow<PoemaEntity?> = _poemaActual.asStateFlow()

    private val _medallasGanadas = MutableStateFlow<Set<String>>(emptySet())
    val medallasGanadas: StateFlow<Set<String>> = _medallasGanadas.asStateFlow()

    private val _cartelera = MutableStateFlow<List<CartelEntity>>(emptyList())
    val cartelera: StateFlow<List<CartelEntity>> = _cartelera.asStateFlow()

    private val _resultadoUltimaFuncion = MutableStateFlow<ResultadoIntento?>(null)
    val resultadoUltimaFuncion: StateFlow<ResultadoIntento?> = _resultadoUltimaFuncion.asStateFlow()

    private val _medallasNuevasUltimaFuncion = MutableStateFlow<Set<String>>(emptySet())
    val medallasNuevasUltimaFuncion: StateFlow<Set<String>> = _medallasNuevasUltimaFuncion.asStateFlow()

    private val _grabando = MutableStateFlow(false)
    val grabando: StateFlow<Boolean> = _grabando.asStateFlow()

    private var esRepasoActual = false

    val fraseBemoActual: String get() = SemillaFrasesBemo.frases.random()
    val catalogoMedallas = SemillaMedallas.catalogo
    val catalogoAvatares = SemillaAvatares.avatares

    private var capturador: CapturadorVoz? = null
    private var reenvioLecturaEnVivo: Job? = null

    // StateFlow estable durante toda la vida del ViewModel: la UI se
    // suscribe una sola vez (collectAsState en el NavHost) y nunca necesita
    // reconectarse a una instancia nueva. Antes, este StateFlow se recreaba
    // cada vez que no habia capturador activo, y como esa version "fantasma"
    // ya estaba siendo observada desde antes de tocar Empezar, la UI se
    // quedaba escuchando al fantasma para siempre en cuanto arrancaba una
    // declamacion real: Bemo nunca se enteraba de la voz.
    private val _lecturaEnVivo = MutableStateFlow<LecturaEnVivo?>(null)
    val lecturaEnVivo: StateFlow<LecturaEnVivo?> = _lecturaEnVivo.asStateFlow()

    init {
        viewModelScope.launch {
            repositorio.sembrarSiEsNecesario()
            _perfil.value = db.perfilDao().obtener()
            _actos.value = repositorio.listarActos()
            _medallasGanadas.value = repositorio.listarMedallasGanadas()
            _cartelera.value = repositorio.listarCartelera()
        }
    }

    fun crearPerfil(alias: String, avatarId: Int) {
        viewModelScope.launch {
            _perfil.value = repositorio.obtenerOCrearPerfil(alias, avatarId)
        }
    }

    fun seleccionarActo(actoId: Int) {
        viewModelScope.launch {
            _poemasDelActo.value = repositorio.listarPoemasDeActo(actoId)
        }
    }

    fun seleccionarPoema(poema: PoemaEntity, esRepaso: Boolean = false) {
        _poemaActual.value = poema
        esRepasoActual = esRepaso
    }

    fun iniciarDeclamacion(): Boolean {
        val poema = _poemaActual.value ?: return false
        val nuevoCapturador = CapturadorVoz(
            context = getApplication(),
            rangoVolumenObjetivo = poema.volumenMinimo..poema.volumenMaximo
        )
        val pudoIniciar = nuevoCapturador.iniciar()
        if (pudoIniciar) {
            capturador = nuevoCapturador
            _grabando.value = true
            reenvioLecturaEnVivo = viewModelScope.launch {
                nuevoCapturador.lecturaEnVivo.collect { _lecturaEnVivo.value = it }
            }
        }
        return pudoIniciar
    }

    fun detenerDeclamacion() {
        val poema = _poemaActual.value ?: return
        val esRepaso = esRepasoActual
        val capturadorActivo = capturador ?: return

        reenvioLecturaEnVivo?.cancel()
        reenvioLecturaEnVivo = null

        val resultado = capturadorActivo.detener(
            rangoRitmoObjetivo = poema.ritmoMinimoSilabasPorMinuto..poema.ritmoMaximoSilabasPorMinuto,
            variacionMinimaTonoHz = VARIACION_MINIMA_TONO_HZ,
            duracionMinimaPausaMs = DURACION_MINIMA_PAUSA_MS,
            pausasEsperadas = poema.marcasPausaCsv.split(",").count { it.isNotBlank() }
        )
        capturador = null
        _grabando.value = false
        _lecturaEnVivo.value = null
        _resultadoUltimaFuncion.value = resultado

        viewModelScope.launch {
            val registro: ResultadoRegistroIntento = repositorio.registrarIntento(
                poema = poema,
                resultado = resultado,
                hoy = LocalDate.now(),
                esRepaso = esRepaso
            )
            _medallasNuevasUltimaFuncion.value = registro.medallasNuevas
            _medallasGanadas.value = repositorio.listarMedallasGanadas()
            _cartelera.value = repositorio.listarCartelera()
            _actos.value = repositorio.listarActos()
            _poemaActual.value?.let { poemaActualizado ->
                _poemasDelActo.value = repositorio.listarPoemasDeActo(poemaActualizado.actoId)
            }
        }
    }

    companion object {
        private const val VARIACION_MINIMA_TONO_HZ = 15f
        private const val DURACION_MINIMA_PAUSA_MS = 400L
    }
}
