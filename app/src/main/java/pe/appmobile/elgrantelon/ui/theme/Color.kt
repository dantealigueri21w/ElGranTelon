package pe.appmobile.elgrantelon.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta de teatro clásico (ficha, sección "Mundo e identidad visual").
// Contraste WCAG verificado a mano antes de fijarla: el texto va SIEMPRE en
// TextoPrincipal (14.1:1 sobre Fondo). Dorado y Verde nunca se usan como texto
// sobre Fondo (1.8:1 y 4.0:1 respectivamente, ambos bajo el mínimo de 4.5:1) —
// solo como relleno de ilustraciones, iconos y superficies grandes.
val CortinaRoja = Color(0xFF7A2333)
val BambalinasAzulNegro = Color(0xFF1C1B2E)
val DoradoReflector = Color(0xFFE3A730)
val VerdeTelonLateral = Color(0xFF3F7D6B)
val FondoCrema = Color(0xFFF4EAD5)

// Rojo de error, distinto de CortinaRoja para no confundir "acento cálido" con
// "aviso de fallo". Verificado: 5.5:1 sobre FondoCrema, pasa el mínimo.
val RojoError = Color(0xFFB3261E)

val TextoPrincipal = BambalinasAzulNegro
