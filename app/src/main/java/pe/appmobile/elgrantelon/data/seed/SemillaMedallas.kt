package pe.appmobile.elgrantelon.data.seed

data class DefinicionMedalla(
    val id: String,
    val nombre: String,
    val descripcion: String
)

object SemillaMedallas {
    val catalogo = listOf(
        DefinicionMedalla("primera_funcion", "Primera Función", "Declamaste tu primer poema completo."),
        DefinicionMedalla("voz_de_trueno", "Voz de Trueno", "Volumen adecuado sostenido en un poema entero."),
        DefinicionMedalla("buen_oido", "Buen Oído", "Entonación con variación real en tres poemas."),
        DefinicionMedalla("paso_de_actor", "Paso de Actor", "Ritmo estable, sin acelerones, en tres poemas."),
        DefinicionMedalla("silencio_de_oro", "Silencio de Oro", "Respetaste todas las pausas marcadas de un poema."),
        DefinicionMedalla("funcion_llena", "Función Llena", "Completaste un Acto entero."),
        DefinicionMedalla("bis", "Bis", "Repetiste un poema ya dominado y mejoraste el resultado."),
        DefinicionMedalla("voz_propia", "Voz Propia", "Completaste tu primer reto de la Función de Repaso."),
        DefinicionMedalla("telon_de_gala", "Telón de Gala", "Completaste los tres Actos: el teatro a máximo brillo.")
    )
}
