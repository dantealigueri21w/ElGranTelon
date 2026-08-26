package pe.appmobile.elgrantelon.data.seed

import pe.appmobile.elgrantelon.data.entity.PoemaEntity

object SemillaPoemas {

    private const val AUTOR_ORIGINAL = "Original — El Gran Telón"

    // Acto 1 — Primeros pasos: volumen y pausas, versos cortos
    private val actoUno = listOf(
        PoemaEntity(
            id = 1, actoId = 1, titulo = "El primer aplauso", autor = AUTOR_ORIGINAL,
            texto = "Se abre el telón, se enciende la luz,\n" +
                "mi voz todavía no sabe qué hacer.\n" +
                "Respiro despacio y subo la voz:\n" +
                "el primer aplauso ya empieza a nacer.",
            marcasPausaCsv = "0",
            volumenMinimo = 0.08f, volumenMaximo = 0.30f,
            ritmoMinimoSilabasPorMinuto = 100, ritmoMaximoSilabasPorMinuto = 180
        ),
        PoemaEntity(
            id = 2, actoId = 1, titulo = "Buenos días, telón", autor = AUTOR_ORIGINAL,
            texto = "Buenos días, telón, buenos días, lugar,\n" +
                "hoy vengo a decirte lo que sé contar.\n" +
                "No tengo miedo, o tengo muy poco:\n" +
                "lo digo despacio, y después lo digo todo.",
            marcasPausaCsv = "1",
            volumenMinimo = 0.08f, volumenMaximo = 0.30f,
            ritmoMinimoSilabasPorMinuto = 100, ritmoMaximoSilabasPorMinuto = 180
        ),
        PoemaEntity(
            id = 3, actoId = 1, titulo = "Mi voz despierta", autor = AUTOR_ORIGINAL,
            texto = "Mi voz estaba dormida en un rincón,\n" +
                "la desperté con una buena respiración.\n" +
                "Ahora camina, ahora se hace grande,\n" +
                "y llena el escenario de punta a punta.",
            marcasPausaCsv = "0",
            volumenMinimo = 0.08f, volumenMaximo = 0.30f,
            ritmoMinimoSilabasPorMinuto = 100, ritmoMaximoSilabasPorMinuto = 180
        ),
        PoemaEntity(
            id = 4, actoId = 1, titulo = "Silencio, por favor", autor = AUTOR_ORIGINAL,
            texto = "Silencio, por favor, un momento nada más,\n" +
                "que el silencio también sabe hablar.\n" +
                "Cuando me callo, ustedes escuchan mejor:\n" +
                "el silencio es parte de mi voz interior.",
            marcasPausaCsv = "0",
            volumenMinimo = 0.08f, volumenMaximo = 0.30f,
            ritmoMinimoSilabasPorMinuto = 100, ritmoMaximoSilabasPorMinuto = 180
        )
    )

    // Acto 2 — Subiendo al escenario: entonacion y ritmo, versos medios
    private val actoDos = listOf(
        PoemaEntity(
            id = 5, actoId = 2, titulo = "La pregunta y la respuesta", autor = AUTOR_ORIGINAL,
            texto = "¿Quién vive detrás de este telón?\n" +
                "¿Quién guarda la luz en su corazón?\n" +
                "Soy yo, que declamo sin ningún temor,\n" +
                "soy yo, que le pongo música a mi voz.\n" +
                "Pregunto primero, después respondo,\n" +
                "así aprendo a subir y a bajar el tono.",
            marcasPausaCsv = "1,3",
            volumenMinimo = 0.10f, volumenMaximo = 0.35f,
            ritmoMinimoSilabasPorMinuto = 140, ritmoMaximoSilabasPorMinuto = 220
        ),
        PoemaEntity(
            id = 6, actoId = 2, titulo = "El tambor del corazón", autor = AUTOR_ORIGINAL,
            texto = "Tan-tan, tan-tan, suena mi corazón,\n" +
                "marca el compás de mi declamación.\n" +
                "No corro tanto, no voy tan despacio,\n" +
                "encuentro el paso justo en este espacio.\n" +
                "Tan-tan, tan-tan, así debo seguir,\n" +
                "ni muy callado ni a punto de reír.",
            marcasPausaCsv = "1,3",
            volumenMinimo = 0.10f, volumenMaximo = 0.35f,
            ritmoMinimoSilabasPorMinuto = 140, ritmoMaximoSilabasPorMinuto = 220
        ),
        PoemaEntity(
            id = 7, actoId = 2, titulo = "Sube y baja", autor = AUTOR_ORIGINAL,
            texto = "Cuando pregunto, mi voz sube un poco,\n" +
                "como una escalera que no tiene freno.\n" +
                "Cuando respondo, mi voz baja despacio,\n" +
                "como una hoja que cae del árbol lento.\n" +
                "Subo, bajo, subo otra vez,\n" +
                "así mi voz aprende a moverse bien.",
            marcasPausaCsv = "1,3",
            volumenMinimo = 0.10f, volumenMaximo = 0.35f,
            ritmoMinimoSilabasPorMinuto = 140, ritmoMaximoSilabasPorMinuto = 220
        ),
        PoemaEntity(
            id = 8, actoId = 2, titulo = "El eco del bosque", autor = AUTOR_ORIGINAL,
            texto = "Grité en el bosque y el bosque contestó,\n" +
                "con la misma fuerza que mi voz le dio.\n" +
                "Si grito fuerte, fuerte vuelve el sonido,\n" +
                "si hablo despacio, despacio es respondido.\n" +
                "El escenario también es como el bosque:\n" +
                "me devuelve exactamente lo que le doy.",
            marcasPausaCsv = "1,3",
            volumenMinimo = 0.10f, volumenMaximo = 0.35f,
            ritmoMinimoSilabasPorMinuto = 140, ritmoMaximoSilabasPorMinuto = 220
        ),
        PoemaEntity(
            id = 9, actoId = 2, titulo = "Paso a paso", autor = AUTOR_ORIGINAL,
            texto = "Un paso, dos pasos, subo al escenario,\n" +
                "tres pasos, cuatro, y ya estoy en mi sitio.\n" +
                "No hay que correr para llegar primero,\n" +
                "hay que llegar entero, paso a paso, entero.\n" +
                "Un paso, dos pasos, mi voz va con calma,\n" +
                "tres pasos, cuatro, y el público me aclama.",
            marcasPausaCsv = "1,3",
            volumenMinimo = 0.10f, volumenMaximo = 0.35f,
            ritmoMinimoSilabasPorMinuto = 140, ritmoMaximoSilabasPorMinuto = 220
        )
    )

    // Acto 3 — Gran Funcion: integra las cuatro variables, versos mas largos
    private val actoTres = listOf(
        PoemaEntity(
            id = 10, actoId = 3, titulo = "La luciérnaga valiente", autor = AUTOR_ORIGINAL,
            texto = "Había una luciérnaga sin su luz,\n" +
                "en un teatro apagado y sin voz.\n" +
                "Esperaba que alguien subiera al lugar,\n" +
                "que se atreviera de nuevo a declamar.\n" +
                "Yo llegué, respiré, y empecé a hablar,\n" +
                "y su luz, poco a poco, volvió a brillar.\n" +
                "Ahora vuela más alto cuando alzo la voz,\n" +
                "y se queda quieta cuando guardo la pausa yo.\n" +
                "Juntos llenamos el teatro de luz,\n" +
                "ella con su brillo, yo con mi voz.",
            marcasPausaCsv = "1,5,7",
            volumenMinimo = 0.12f, volumenMaximo = 0.40f,
            ritmoMinimoSilabasPorMinuto = 160, ritmoMaximoSilabasPorMinuto = 260
        ),
        PoemaEntity(
            id = 11, actoId = 3, titulo = "El teatro que despertó", autor = AUTOR_ORIGINAL,
            texto = "Este teatro dormía desde hace tiempo,\n" +
                "con las butacas vacías y el polvo por dentro.\n" +
                "Nadie subía, nadie decía nada,\n" +
                "hasta que una voz cruzó la entrada.\n" +
                "Esa voz era la mía, temblando al empezar,\n" +
                "pero encontró su fuerza al seguir declamando.\n" +
                "El telón se abrió como un ojo que despierta,\n" +
                "y el teatro entero volvió a estar alerta.\n" +
                "Ahora cada tarde alguien sube a contar,\n" +
                "y el teatro que dormía ya no deja de brillar.",
            marcasPausaCsv = "1,5,7",
            volumenMinimo = 0.12f, volumenMaximo = 0.40f,
            ritmoMinimoSilabasPorMinuto = 160, ritmoMaximoSilabasPorMinuto = 260
        ),
        PoemaEntity(
            id = 12, actoId = 3, titulo = "Gracias, público", autor = AUTOR_ORIGINAL,
            texto = "Gracias, público, por su atención,\n" +
                "por el silencio que me dio ocasión.\n" +
                "Gracias por las manos que después aplaudieron,\n" +
                "por los ojos atentos que en mí se detuvieron.\n" +
                "No fue perfecto, no fue el mejor día,\n" +
                "pero puse mi voz, mi ritmo y mi alegría.\n" +
                "Cae el telón, se enciende la sala,\n" +
                "y en mi corazón todavía queda mi palabra.",
            marcasPausaCsv = "1,3,5",
            volumenMinimo = 0.12f, volumenMaximo = 0.40f,
            ritmoMinimoSilabasPorMinuto = 160, ritmoMaximoSilabasPorMinuto = 260
        ),
        PoemaEntity(
            id = 13, actoId = 3, titulo = "Cultivo una rosa blanca", autor = "José Martí (dominio público)",
            texto = "Cultivo una rosa blanca\n" +
                "en julio como en enero,\n" +
                "para el amigo sincero\n" +
                "que me da su mano franca.\n" +
                "Y para el cruel que me arranca\n" +
                "el corazón con que vivo,\n" +
                "cardo ni ortiga cultivo:\n" +
                "cultivo la rosa blanca.",
            marcasPausaCsv = "3,5",
            volumenMinimo = 0.12f, volumenMaximo = 0.40f,
            ritmoMinimoSilabasPorMinuto = 160, ritmoMaximoSilabasPorMinuto = 260
        )
    )

    val poemas: List<PoemaEntity> = actoUno + actoDos + actoTres
}
