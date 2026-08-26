package pe.appmobile.elgrantelon.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.data.entity.IntentoDeclamacionEntity
import pe.appmobile.elgrantelon.data.entity.MedallaEntity
import pe.appmobile.elgrantelon.data.entity.PerfilEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity

@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase

    @Before
    fun crear() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun cerrar() {
        db.close()
    }

    @Test
    fun `guardar y leer un perfil`() = runTest {
        db.perfilDao().guardar(PerfilEntity(alias = "Explorador", avatarId = 3))
        assertEquals("Explorador", db.perfilDao().obtener()?.alias)
    }

    @Test
    fun `insertar actos y listarlos ordenados`() = runTest {
        db.actoDao().insertarTodos(listOf(
            ActoEntity(id = 2, nombre = "Subiendo al escenario", orden = 2, desbloqueado = false),
            ActoEntity(id = 1, nombre = "Primeros pasos", orden = 1, desbloqueado = true)
        ))
        val actos = db.actoDao().listarTodos()
        assertEquals(2, actos.size)
        assertEquals(1, actos[0].orden)
    }

    @Test
    fun `guardar un intento de declamacion y consultarlo por poema`() = runTest {
        db.poemaDao().insertarTodos(listOf(poemaDePrueba()))
        db.intentoDeclamacionDao().insertar(intentoDePrueba(poemaId = 1))
        assertEquals(1, db.intentoDeclamacionDao().listarPorPoema(1).size)
    }

    @Test
    fun `las medallas duplicadas no se insertan dos veces`() = runTest {
        db.medallaDao().guardar(MedallaEntity(id = "primera_funcion", fechaObtencionEpochDay = 1L))
        db.medallaDao().guardar(MedallaEntity(id = "primera_funcion", fechaObtencionEpochDay = 2L))
        assertEquals(1, db.medallaDao().listarGanadas().size)
    }

    private fun poemaDePrueba() = PoemaEntity(
        id = 1, actoId = 1, titulo = "El primer aplauso", autor = "Original — El Gran Telón",
        texto = "linea uno\nlinea dos", marcasPausaCsv = "0",
        volumenMinimo = 0.1f, volumenMaximo = 0.4f,
        ritmoMinimoSilabasPorMinuto = 150, ritmoMaximoSilabasPorMinuto = 250
    )

    private fun intentoDePrueba(poemaId: Int) = IntentoDeclamacionEntity(
        poemaId = poemaId, fechaEpochDay = 20000L, volumenPromedio = 0.25f, volumenAdecuado = true,
        entonacionAdecuada = true, silabasPorMinuto = 200, ritmoAdecuado = true,
        pausasRespetadas = 2, pausasEsperadas = 2, aprobado = true, contornoTonoCsv = "200.0,210.0"
    )
}
