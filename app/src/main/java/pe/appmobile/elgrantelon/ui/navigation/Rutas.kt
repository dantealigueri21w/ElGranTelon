package pe.appmobile.elgrantelon.ui.navigation

object Rutas {
    const val CAMERINO = "camerino"
    const val TEATRO = "teatro"
    const val PROGRAMA_DE_MANO = "programa/{actoId}"
    const val ATRIL = "atril/{poemaId}"
    const val ESCENARIO = "escenario"
    const val CAE_EL_TELON = "telon"
    const val CARTELERA = "cartelera"
    const val VITRINA = "vitrina"
    const val FUNCION_DE_REPASO = "repaso"
    const val AJUSTES = "ajustes"

    fun programaDeMano(actoId: Int) = "programa/$actoId"
    fun atril(poemaId: Int) = "atril/$poemaId"
}
