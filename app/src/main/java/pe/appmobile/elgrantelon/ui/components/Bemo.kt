package pe.appmobile.elgrantelon.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.domain.model.NivelVolumen
import pe.appmobile.elgrantelon.ui.theme.BambalinasAzulNegro
import pe.appmobile.elgrantelon.ui.theme.DoradoReflector

private const val F0_MINIMO_HZ = 75f
private const val F0_MAXIMO_HZ = 500f

// Bemo no es una ilustracion fija: ES el reflector, y su comportamiento en este
// Canvas es la mecanica central de la app (ficha, "Mundo e identidad visual").
// El arte final de Gemini reemplaza este dibujo mas adelante; el mapeo brillo/
// altura/aleteo a las cuatro variables acusticas se queda igual.
@Composable
fun Bemo(
    nivelVolumen: NivelVolumen?,
    tonoHz: Float?,
    modifier: Modifier = Modifier
) {
    val brilloObjetivo = when (nivelVolumen) {
        NivelVolumen.BAJO -> 0.3f
        NivelVolumen.ADECUADO -> 0.85f
        NivelVolumen.ALTO -> 1f
        null -> 0.15f
    }
    val brillo by animateFloatAsState(targetValue = brilloObjetivo, label = "brilloBemo")

    val alturaObjetivo = tonoHz
        ?.let { hz -> (hz - F0_MINIMO_HZ) / (F0_MAXIMO_HZ - F0_MINIMO_HZ) }
        ?.coerceIn(0f, 1f)
        ?: 0.5f
    val altura by animateFloatAsState(targetValue = alturaObjetivo, label = "alturaBemo")

    val descripcion = when (nivelVolumen) {
        null -> "Bemo, apagado, esperando tu voz"
        NivelVolumen.BAJO -> "Bemo brilla tenue: sube un poco la voz"
        NivelVolumen.ALTO -> "Bemo brilla muy fuerte: baja un poco la voz"
        NivelVolumen.ADECUADO -> "Bemo brilla con fuerza justa"
    }

    Canvas(
        modifier = modifier
            .size(160.dp)
            .semantics { contentDescription = descripcion }
    ) {
        val radioBase = size.minDimension / 6f
        val margenVertical = radioBase * 3f
        val centroX = size.width / 2f
        val centroY = margenVertical + (size.height - margenVertical * 2f) * (1f - altura)
        val centro = Offset(centroX, centroY)

        drawCircle(
            color = DoradoReflector.copy(alpha = brillo * 0.35f),
            radius = radioBase * (1.8f + brillo),
            center = centro
        )
        drawCircle(
            color = DoradoReflector.copy(alpha = 0.4f + brillo * 0.6f),
            radius = radioBase,
            center = centro
        )
        drawCircle(
            color = BambalinasAzulNegro,
            radius = radioBase * 0.35f,
            center = centro
        )
    }
}
