# Bitácora de compilación — El Gran Telón

## 25/08/2026 — Inicio del proyecto (Parte 1: dominio y datos)

Scaffold creado siguiendo `01-PROMPT-MAESTRO.md` sección 7 y 7.1.
Versiones: Gradle 9.3.1, AGP 9.1.1, Kotlin 2.4.0, Compose BOM 2026.08.00,
Room 2.8.4, JDK 17, compileSdk/targetSdk 37, minSdk 24.

`.gitignore` sin nombres de herramientas de desarrollo; las carpetas de
configuración correspondientes se excluyen vía `.git/info/exclude`, que
no se comitea.

## 26/08/2026 — Parte 1 completa: dominio y datos

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **62 tests, 0 fallos, 0 errores**
  (verificado desde estado limpio, no incremental).
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- Motores (`domain/engine/`, 44 tests): `MotorVolumen` (10), `MotorEntonacion` (8),
  `MotorRitmo` (6), `MotorPausas` (5), `MotorEvaluacionFuncion` (6), `MotorProgreso` (6).
  Todos probados con audio sintético (tonos puros, silencios, ruido generados
  matemáticamente), nunca con grabaciones reales.
  Corrección aplicada durante la implementación: `MotorEntonacion` originalmente
  buscaba el lag de mayor correlación en todo el rango de búsqueda, lo que a veces
  confundía un armónico (múltiplo entero del tono real) con el tono fundamental —
  verificado numéricamente antes de corregir. La versión final busca el primer pico
  local desde la frecuencia más aguda hacia abajo, el mismo principio que usa YIN
  para evitar errores de octava.
- Datos (`data/`, 18 tests): 7 entidades de Room, 7 DAO, `AppDatabase` (Robolectric,
  en memoria), `ElGranTelonRepository` (une datos y dominio: registra intentos,
  desbloquea actos, calcula racha y medallas nuevas contra el historial real).
- Datos semilla: 3 actos, **13 poemas** (12 originales + 1 de dominio público
  verificado por texto real: José Martí, "Cultivo una rosa blanca"), 9 medallas,
  8 avatares, 18 frases de Bemo.
- `database/schema.sql` generado a partir del esquema real exportado por Room
  (`app/schemas/`, no escrito a mano). `database/sample_data.sql` con los 3 actos
  y 13 poemas reales.
- Verificación de higiene (secciones 9 y 11.4 del prompt maestro): sin permiso
  `INTERNET` (único permiso: `RECORD_AUDIO`), sin menciones de herramientas de IA
  en ningún archivo, identidad de git uniforme en los 16 commits
  (`dantealigueri21w`), árbol de trabajo limpio.
- Pendiente para la Parte 2: tema y paleta, componentes de Compose, las 9
  pantallas, navegación, captura real de audio con `AudioRecord`,
  integración del arte ilustrado final.

## 26/08/2026 — Parte 2 completa: interfaz, navegación y captura real de voz

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **82 tests, 0 fallos, 0 errores**
  (verificado desde estado limpio).
- `./gradlew lintDebug`: BUILD SUCCESSFUL, 0 errores, 13 advertencias benignas
  (versiones de dependencias más nuevas disponibles, ícono de la app pendiente
  para la fase de arte, `allowBackup` deprecado desde Android 12 — ninguna
  bloqueante). Se corrigió un error real de `MissingPermission` (lint) sobre
  `CapturadorVoz.kt`: el chequeo de permiso vivía en una función aparte y el
  analizador estático no lo rastreaba hasta la llamada a `AudioRecord`; se
  puso el chequeo en línea justo antes de la llamada.
- `./gradlew assembleDebug`: BUILD SUCCESSFUL, APK de **20 MB** (sin arte
  todavía — dentro del rango 15-60 MB esperado).
- Tema visual: paleta de teatro clásico con el contraste WCAG verificado a
  mano antes de fijarla (dorado y verde nunca como texto sobre el fondo,
  sección 6.1).
- `MotorCaptura` (nuevo, `domain/engine/`): orquesta los cuatro motores
  durante una declamación en curso, publica una lectura cada ~160 ms
  (dentro del rango 150-200 ms decidido con el cliente). Probado con audio
  sintético, 6 tests.
- `CapturadorVoz` (`audio/`): el único punto del proyecto que toca
  `android.media.AudioRecord` — captura real a 16 kHz, ventanas de 40 ms,
  alimenta `MotorCaptura`. Sin test unitario propio (no hay forma de simular
  un micrófono real en la JVM); se apoya en `MotorCaptura`, que sí está
  probado.
- Las 9 pantallas (Camerino, El Teatro, Programa de mano, El Atril, El
  Escenario, Cae el Telón, Cartelera, Vitrina de Medallas, Función de
  Repaso) más Ajustes, conectadas con Navigation Compose y un solo
  `ElGranTelonViewModel`. Ninguna usa arte todavía: todo es color y forma
  (Canvas, formas propias) — Bemo mismo se dibuja con `Canvas`, reaccionando
  en vivo a volumen y entonación reales.
- Cada una de las 9 pantallas tiene su prueba de Compose que la renderiza de
  verdad (sección 10.1) — 13 tests en `PantallasSinCrashTest.kt`. Encontró
  y corrigió un error real de estructura de pruebas (`setContent` llamado
  dos veces en el mismo test), no un error de las pantallas.
- Permiso `RECORD_AUDIO` pedido en contexto, al tocar "Empezar" en El
  Escenario (no al entrar a la pantalla) — sigue siendo utilizable en modo
  lectura si se deniega.
- Verificación de higiene: `grep` de herramientas de IA (sección 11.4) dio
  dos coincidencias reales, ambas nombrando por error la herramienta de
  generación de imágenes al referirse al flujo de arte — corregidas antes
  de este commit. Identidad de git uniforme en los 28 commits
  (`dantealigueri21w`).
- Pendiente: integrar el arte ilustrado final (se genera por fuera del
  repositorio, siguiendo `02-GUIA-IMAGENES.md`) y el icono del lanzador. La
  app es completamente jugable sin ellos.

## 26/08/2026 — Arte integrado en las 9 pantallas

- Los 28 recursos aprobados (`drawable-nodpi/`) ya no están sueltos: se
  conectaron a las pantallas reales a través de `ui/RecursosArte.kt`
  (mapeo `avatarDrawable`, `medallaDrawable`, `actoFondoDrawable`).
  - Camerino: los 8 avatares numerados en círculo pasaron a ser las
    ilustraciones reales, recortadas en círculo.
  - Teatro: cada Acto muestra su fondo ilustrado detrás de una capa de
    color semitransparente (mantiene el contraste del texto); los accesos
    a Camerino/Ajustes/Cartelera/Vitrina usan los íconos de módulo en
    lugar de los íconos genéricos de Material.
  - Vitrina de medallas: las 9 medallas ilustradas reemplazan las cajas de
    color; una medalla no ganada se atenúa (alpha 0.3) en vez de mostrar
    la palabra "Bloqueada" sola.
  - Cartelera: `plantilla_cartel` como fondo de cada tarjeta de logro.
  - Programa de mano, Función de repaso y Ajustes: ícono de módulo junto
    al título de cada pantalla.
- `./gradlew compileDebugKotlin`: BUILD SUCCESSFUL.
- `./gradlew testDebugUnitTest`: BUILD SUCCESSFUL, **82 tests, 0 fallos**
  (las 13 pruebas de `PantallasSinCrashTest.kt` siguen renderizando las 9
  pantallas sin excepción, ahora con imágenes reales en vez de placeholders).
- `./gradlew lintDebug`: BUILD SUCCESSFUL, 0 errores.
- `./gradlew assembleDebug`: BUILD SUCCESSFUL, APK de **20 MB** (el arte en
  WebP no movió sensiblemente el tamaño).
- Pendiente todavía: pulir 2 de los 8 recortes de Bemo con residuos de
  fondo (`bemo_posada`, `bemo_vuelo_alto`) — no bloqueante porque Bemo se
  sigue dibujando con `Canvas`, no con esas imágenes.

## 26/08/2026 — Ícono del lanzador y cierre

- Ícono adaptativo (`mipmap-anydpi-v26/ic_launcher.xml` + `_round.xml`):
  fondo sólido `#7A2333` (el mismo rojo cortina del ícono fuente, vector
  en `drawable/ic_launcher_background.xml`) con la ilustración de las
  cortinas y el destello dorado como capa de primer plano
  (`drawable-nodpi/icono_lanzador.webp`, recortada a 512×512). El icono
  fuente ya traía ~18-20% de margen propio en las cuatro esquinas —igual
  a la zona segura estándar de Android—, así que se usó `inset="0%"` en
  vez del 18% habitual de las apps hermanas, para no aplicar el margen
  dos veces y terminar con las cortinas diminutas. `android:icon` quedó
  enlazado en `AndroidManifest.xml`.
- Se corrigió que la rama local se llamaba `master` mientras el workflow
  `.github/workflows/android-build.yml` solo dispara en push a `main`
  (el mismo desajuste existe en otras apps del lote) — renombrada a
  `main` para que el primer push genere el APK sin pasos manuales.
- `./gradlew lintDebug`, `testDebugUnitTest` (82 tests, 0 fallos) y
  `assembleDebug`: BUILD SUCCESSFUL los tres, desde este mismo estado
  con el ícono ya enlazado.
- La app queda completa: dominio y datos, interfaz conectada al arte
  aprobado, captura de voz real, e ícono de lanzador propio. Sin
  pendientes bloqueantes.
