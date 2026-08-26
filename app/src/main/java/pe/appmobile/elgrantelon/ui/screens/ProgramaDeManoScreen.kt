package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.ui.theme.DoradoReflector
import pe.appmobile.elgrantelon.ui.theme.VerdeTelonLateral

@Composable
fun ProgramaDeManoScreen(
    poemas: List<PoemaEntity>,
    onSeleccionarPoema: (PoemaEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Programa de mano", style = MaterialTheme.typography.headlineMedium)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            poemas.forEach { poema ->
                FilaPoema(poema = poema, onSeleccionar = { onSeleccionarPoema(poema) })
            }
        }
    }
}

@Composable
private fun FilaPoema(poema: PoemaEntity, onSeleccionar: () -> Unit) {
    val colorFondo = if (poema.dominado) VerdeTelonLateral else MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(colorFondo, RoundedCornerShape(12.dp))
            .clickable { onSeleccionar() }
            .semantics {
                contentDescription = "${poema.titulo}, " + if (poema.dominado) "dominado" else "por declamar"
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(poema.titulo, color = MaterialTheme.colorScheme.onSecondary, style = MaterialTheme.typography.titleLarge)
            if (poema.dominado) {
                Text("Dominado", color = DoradoReflector)
            }
        }
    }
}
