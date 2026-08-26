package pe.appmobile.elgrantelon.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.appmobile.elgrantelon.data.seed.SemillaActos
import pe.appmobile.elgrantelon.data.seed.SemillaPoemas

class SemillaContractTest {

    @Test
    fun `hay exactamente 3 actos`() {
        assertEquals(3, SemillaActos.actos.size)
    }

    @Test
    fun `hay exactamente 13 poemas, 4-5-4 por acto`() {
        assertEquals(13, SemillaPoemas.poemas.size)
        assertEquals(4, SemillaPoemas.poemas.count { it.actoId == 1 })
        assertEquals(5, SemillaPoemas.poemas.count { it.actoId == 2 })
        assertEquals(4, SemillaPoemas.poemas.count { it.actoId == 3 })
    }

    @Test
    fun `todos los ids de poema son unicos`() {
        val ids = SemillaPoemas.poemas.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `cada poema tiene umbrales de volumen y ritmo validos`() {
        SemillaPoemas.poemas.forEach { poema ->
            assertTrue("poema ${poema.id}: volumenMinimo debe ser menor que volumenMaximo",
                poema.volumenMinimo < poema.volumenMaximo)
            assertTrue("poema ${poema.id}: ritmoMinimo debe ser menor que ritmoMaximo",
                poema.ritmoMinimoSilabasPorMinuto < poema.ritmoMaximoSilabasPorMinuto)
        }
    }

    @Test
    fun `cada marca de pausa apunta a una linea real del poema`() {
        SemillaPoemas.poemas.forEach { poema ->
            val totalLineas = poema.texto.split("\n").size
            val marcas = poema.marcasPausaCsv.split(",").filter { it.isNotBlank() }.map { it.toInt() }
            marcas.forEach { indice ->
                assertTrue("poema ${poema.id}: marca de pausa $indice fuera de rango (0..${totalLineas - 1})",
                    indice in 0 until totalLineas)
            }
        }
    }

    @Test
    fun `el poema de dominio publico es el de Marti y aparece solo una vez`() {
        val deMarti = SemillaPoemas.poemas.filter { it.autor.contains("Martí") }
        assertEquals(1, deMarti.size)
        assertEquals("Cultivo una rosa blanca", deMarti.first().titulo)
    }
}
