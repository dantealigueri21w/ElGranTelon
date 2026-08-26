package pe.appmobile.elgrantelon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tema claro único (el manifiesto ya fija Theme.Material.Light.NoActionBar):
// no se planea modo oscuro para esta app.
private val ElGranTelonColorScheme = lightColorScheme(
    primary = CortinaRoja,
    onPrimary = FondoCrema,
    secondary = BambalinasAzulNegro,
    onSecondary = FondoCrema,
    tertiary = DoradoReflector,
    onTertiary = BambalinasAzulNegro,
    background = FondoCrema,
    onBackground = TextoPrincipal,
    surface = FondoCrema,
    onSurface = TextoPrincipal,
    error = RojoError,
    onError = FondoCrema
)

private val ElGranTelonTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextoPrincipal),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextoPrincipal),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 18.sp, color = TextoPrincipal),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, color = TextoPrincipal)
)

@Composable
fun ElGranTelonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ElGranTelonColorScheme,
        typography = ElGranTelonTypography,
        content = content
    )
}
