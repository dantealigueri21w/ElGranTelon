package pe.appmobile.elgrantelon.data.seed

import pe.appmobile.elgrantelon.data.entity.ActoEntity

object SemillaActos {
    val actos = listOf(
        ActoEntity(id = 1, nombre = "Primeros pasos", orden = 1, desbloqueado = true, completado = false),
        ActoEntity(id = 2, nombre = "Subiendo al escenario", orden = 2, desbloqueado = false, completado = false),
        ActoEntity(id = 3, nombre = "Gran Función", orden = 3, desbloqueado = false, completado = false)
    )
}
