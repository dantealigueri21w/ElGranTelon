package pe.appmobile.elgrantelon.data.seed

data class DefinicionAvatar(val id: Int, val nombreReferencia: String)

object SemillaAvatares {
    val avatares = listOf(
        DefinicionAvatar(1, "avatar_uno"),
        DefinicionAvatar(2, "avatar_dos"),
        DefinicionAvatar(3, "avatar_tres"),
        DefinicionAvatar(4, "avatar_cuatro"),
        DefinicionAvatar(5, "avatar_cinco"),
        DefinicionAvatar(6, "avatar_seis"),
        DefinicionAvatar(7, "avatar_siete"),
        DefinicionAvatar(8, "avatar_ocho")
    )
}
