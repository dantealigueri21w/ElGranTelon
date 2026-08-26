-- Datos semilla reales de El Gran Telón: 3 actos, 13 poemas.
-- Volcado directo de SemillaActos.kt y SemillaPoemas.kt.

INSERT INTO `acto` (`id`, `nombre`, `orden`, `desbloqueado`, `completado`) VALUES
(1, 'Primeros pasos', 1, 1, 0),
(2, 'Subiendo al escenario', 2, 0, 0),
(3, 'Gran Función', 3, 0, 0);

INSERT INTO `poema` (`id`, `actoId`, `titulo`, `autor`, `texto`, `marcasPausaCsv`, `volumenMinimo`, `volumenMaximo`, `ritmoMinimoSilabasPorMinuto`, `ritmoMaximoSilabasPorMinuto`, `dominado`) VALUES
(1, 1, 'El primer aplauso', 'Original — El Gran Telón', 'Se abre el telón, se enciende la luz,
mi voz todavía no sabe qué hacer.
Respiro despacio y subo la voz:
el primer aplauso ya empieza a nacer.', '0', 0.08, 0.30, 100, 180, 0),

(2, 1, 'Buenos días, telón', 'Original — El Gran Telón', 'Buenos días, telón, buenos días, lugar,
hoy vengo a decirte lo que sé contar.
No tengo miedo, o tengo muy poco:
lo digo despacio, y después lo digo todo.', '1', 0.08, 0.30, 100, 180, 0),

(3, 1, 'Mi voz despierta', 'Original — El Gran Telón', 'Mi voz estaba dormida en un rincón,
la desperté con una buena respiración.
Ahora camina, ahora se hace grande,
y llena el escenario de punta a punta.', '0', 0.08, 0.30, 100, 180, 0),

(4, 1, 'Silencio, por favor', 'Original — El Gran Telón', 'Silencio, por favor, un momento nada más,
que el silencio también sabe hablar.
Cuando me callo, ustedes escuchan mejor:
el silencio es parte de mi voz interior.', '0', 0.08, 0.30, 100, 180, 0),

(5, 2, 'La pregunta y la respuesta', 'Original — El Gran Telón', '¿Quién vive detrás de este telón?
¿Quién guarda la luz en su corazón?
Soy yo, que declamo sin ningún temor,
soy yo, que le pongo música a mi voz.
Pregunto primero, después respondo,
así aprendo a subir y a bajar el tono.', '1,3', 0.10, 0.35, 140, 220, 0),

(6, 2, 'El tambor del corazón', 'Original — El Gran Telón', 'Tan-tan, tan-tan, suena mi corazón,
marca el compás de mi declamación.
No corro tanto, no voy tan despacio,
encuentro el paso justo en este espacio.
Tan-tan, tan-tan, así debo seguir,
ni muy callado ni a punto de reír.', '1,3', 0.10, 0.35, 140, 220, 0),

(7, 2, 'Sube y baja', 'Original — El Gran Telón', 'Cuando pregunto, mi voz sube un poco,
como una escalera que no tiene freno.
Cuando respondo, mi voz baja despacio,
como una hoja que cae del árbol lento.
Subo, bajo, subo otra vez,
así mi voz aprende a moverse bien.', '1,3', 0.10, 0.35, 140, 220, 0),

(8, 2, 'El eco del bosque', 'Original — El Gran Telón', 'Grité en el bosque y el bosque contestó,
con la misma fuerza que mi voz le dio.
Si grito fuerte, fuerte vuelve el sonido,
si hablo despacio, despacio es respondido.
El escenario también es como el bosque:
me devuelve exactamente lo que le doy.', '1,3', 0.10, 0.35, 140, 220, 0),

(9, 2, 'Paso a paso', 'Original — El Gran Telón', 'Un paso, dos pasos, subo al escenario,
tres pasos, cuatro, y ya estoy en mi sitio.
No hay que correr para llegar primero,
hay que llegar entero, paso a paso, entero.
Un paso, dos pasos, mi voz va con calma,
tres pasos, cuatro, y el público me aclama.', '1,3', 0.10, 0.35, 140, 220, 0),

(10, 3, 'La luciérnaga valiente', 'Original — El Gran Telón', 'Había una luciérnaga sin su luz,
en un teatro apagado y sin voz.
Esperaba que alguien subiera al lugar,
que se atreviera de nuevo a declamar.
Yo llegué, respiré, y empecé a hablar,
y su luz, poco a poco, volvió a brillar.
Ahora vuela más alto cuando alzo la voz,
y se queda quieta cuando guardo la pausa yo.
Juntos llenamos el teatro de luz,
ella con su brillo, yo con mi voz.', '1,5,7', 0.12, 0.40, 160, 260, 0),

(11, 3, 'El teatro que despertó', 'Original — El Gran Telón', 'Este teatro dormía desde hace tiempo,
con las butacas vacías y el polvo por dentro.
Nadie subía, nadie decía nada,
hasta que una voz cruzó la entrada.
Esa voz era la mía, temblando al empezar,
pero encontró su fuerza al seguir declamando.
El telón se abrió como un ojo que despierta,
y el teatro entero volvió a estar alerta.
Ahora cada tarde alguien sube a contar,
y el teatro que dormía ya no deja de brillar.', '1,5,7', 0.12, 0.40, 160, 260, 0),

(12, 3, 'Gracias, público', 'Original — El Gran Telón', 'Gracias, público, por su atención,
por el silencio que me dio ocasión.
Gracias por las manos que después aplaudieron,
por los ojos atentos que en mí se detuvieron.
No fue perfecto, no fue el mejor día,
pero puse mi voz, mi ritmo y mi alegría.
Cae el telón, se enciende la sala,
y en mi corazón todavía queda mi palabra.', '1,3,5', 0.12, 0.40, 160, 260, 0),

(13, 3, 'Cultivo una rosa blanca', 'José Martí (dominio público)', 'Cultivo una rosa blanca
en julio como en enero,
para el amigo sincero
que me da su mano franca.
Y para el cruel que me arranca
el corazón con que vivo,
cardo ni ortiga cultivo:
cultivo la rosa blanca.', '3,5', 0.12, 0.40, 160, 260, 0);
