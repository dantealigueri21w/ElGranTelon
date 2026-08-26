package pe.appmobile.elgrantelon.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.data.repository.ElGranTelonRepository
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class ElGranTelonRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repositorio: ElGranTelonRepository

    private val hoy = LocalDate.of(2026, 8, 25)

    @Before
    fun crear() = runTest {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repositorio = ElGranTelonRepository(db)

        db.actoDao().insertarTodos(listOf(
            ActoEntity(id = 1, nombre = "Acto Uno", orden = 1, desbloqueado = true),
            ActoEntity(id = 2, nombre = "Acto Dos", orden = 2, desbloqueado = false)
        ))
        db.poemaDao().insertarTodos(listOf(
            poema(id = 1, actoId = 1),
            poema(id = 2, actoId = 1)
        ))
    }

    @After
    fun cerrar() {
        db.close()
    }

    private fun poema(id: Int, actoId: Int) = PoemaEntity(
        id = id, actoId = actoId, titulo = "Poema $id", autor = "Original — El Gran Telón",
        texto = "linea uno\nlinea dos", marcasPausaCsv = "0",
        volumenMinimo = 0.1f, volumenMaximo = 0.4f,
        ritmoMinimoSilabasPorMinuto = 150, ritmoMaximoSilabasPorMinuto = 250
    )

    private fun resultadoAprobado(entonacionAdecuada: Boolean = true) = ResultadoIntento(
        volumenPromedio = 0.25f, volumenAdecuado = true,
        contornoTono = listOf(200f, 220f), entonacionAdecuada = entonacionAdecuada,
        silabasPorMinuto = 200, ritmoAdecuado = true,
        pausasRespetadas = 1, pausasEsperadas = 1, aprobado = true
    )

    @Test
    fun `un intento aprobado crea el cartel y marca el poema como dominado`() = runTest {
        val poema1 = db.poemaDao().obtenerPorId(1)!!
        repositorio.registrarIntento(poema1, resultadoAprobado(), hoy)

        assertTrue(db.poemaDao().obtenerPorId(1)!!.dominado)
        assertEquals(1, db.cartelDao().listarTodos().size)
    }

    @Test
    fun `la medalla Primera Funcion se otorga solo la primera vez`() = runTest {
        val poema1 = db.poemaDao().obtenerPorId(1)!!
        val primerResultado = repositorio.registrarIntento(poema1, resultadoAprobado(), hoy)
        assertTrue("primera_funcion" in primerResultado.medallasNuevas)

        val poema2 = db.poemaDao().obtenerPorId(2)!!
        val segundoResultado = repositorio.registrarIntento(poema2, resultadoAprobado(), hoy.plusDays(1))
        assertTrue("primera_funcion" !in segundoResultado.medallasNuevas)
    }

    @Test
    fun `completar todos los poemas de un acto lo marca completado y desbloquea el siguiente`() = runTest {
        val poema1 = db.poemaDao().obtenerPorId(1)!!
        val poema2 = db.poemaDao().obtenerPorId(2)!!

        repositorio.registrarIntento(poema1, resultadoAprobado(), hoy)
        val resultadoFinal = repositorio.registrarIntento(poema2, resultadoAprobado(), hoy)

        assertTrue(resultadoFinal.actoDesbloqueadoSiguiente)
        assertTrue(db.actoDao().obtenerPorId(1)!!.completado)
        assertTrue(db.actoDao().obtenerPorId(2)!!.desbloqueado)
    }

    @Test
    fun `un intento en modo repaso aprobado otorga Voz Propia solo la primera vez`() = runTest {
        val poema1 = db.poemaDao().obtenerPorId(1)!!
        repositorio.registrarIntento(poema1, resultadoAprobado(), hoy)

        val primerRepaso = repositorio.registrarIntento(poema1, resultadoAprobado(), hoy.plusDays(1), esRepaso = true)
        assertTrue("voz_propia" in primerRepaso.medallasNuevas)

        val segundoRepaso = repositorio.registrarIntento(poema1, resultadoAprobado(), hoy.plusDays(2), esRepaso = true)
        assertTrue("voz_propia" !in segundoRepaso.medallasNuevas)
    }

    @Test
    fun `la racha avanza en dias consecutivos a traves de registrarIntento`() = runTest {
        val poema1 = db.poemaDao().obtenerPorId(1)!!
        repositorio.registrarIntento(poema1, resultadoAprobado(), hoy)
        repositorio.registrarIntento(poema1, resultadoAprobado(), hoy.plusDays(1))

        assertEquals(2, db.rachaDao().obtener()!!.diasConsecutivos)
    }

    @Test
    fun `sembrarSiEsNecesario carga los actos y poemas reales solo la primera vez`() = runTest {
        val dbVacia = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repositorioVacio = ElGranTelonRepository(dbVacia)

        repositorioVacio.sembrarSiEsNecesario()
        assertEquals(3, dbVacia.actoDao().listarTodos().size)
        assertEquals(13, dbVacia.poemaDao().listarTodos().size)

        // segunda llamada no debe duplicar filas
        repositorioVacio.sembrarSiEsNecesario()
        assertEquals(13, dbVacia.poemaDao().listarTodos().size)

        dbVacia.close()
    }
}
