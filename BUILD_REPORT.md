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
  integración del arte generado con Gemini.
