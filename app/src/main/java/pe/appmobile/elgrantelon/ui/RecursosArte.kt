package pe.appmobile.elgrantelon.ui

import pe.appmobile.elgrantelon.R

// Mapea los ids de datos (avatar, medalla, acto) a los recursos de arte
// generados en la carpeta arte/ del paquete y ya integrados en
// drawable-nodpi/. Centralizado aca para que ninguna pantalla tenga que
// repetir el mapeo.

fun avatarDrawable(avatarId: Int): Int = when (avatarId) {
    1 -> R.drawable.avatar_uno
    2 -> R.drawable.avatar_dos
    3 -> R.drawable.avatar_tres
    4 -> R.drawable.avatar_cuatro
    5 -> R.drawable.avatar_cinco
    6 -> R.drawable.avatar_seis
    7 -> R.drawable.avatar_siete
    else -> R.drawable.avatar_ocho
}

fun medallaDrawable(medallaId: String): Int = when (medallaId) {
    "primera_funcion" -> R.drawable.medalla_primera_funcion
    "voz_de_trueno" -> R.drawable.medalla_voz_de_trueno
    "buen_oido" -> R.drawable.medalla_buen_oido
    "paso_de_actor" -> R.drawable.medalla_paso_de_actor
    "silencio_de_oro" -> R.drawable.medalla_silencio_de_oro
    "funcion_llena" -> R.drawable.medalla_funcion_llena
    "bis" -> R.drawable.medalla_bis
    "voz_propia" -> R.drawable.medalla_voz_propia
    else -> R.drawable.medalla_telon_de_gala
}

fun actoFondoDrawable(orden: Int): Int = when (orden) {
    1 -> R.drawable.fondo_acto_uno
    2 -> R.drawable.fondo_acto_dos
    else -> R.drawable.fondo_acto_tres
}
