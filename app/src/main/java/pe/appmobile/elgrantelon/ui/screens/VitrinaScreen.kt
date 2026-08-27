package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.R
import pe.appmobile.elgrantelon.data.seed.DefinicionMedalla
import pe.appmobile.elgrantelon.ui.medallaDrawable
import pe.appmobile.elgrantelon.ui.theme.DoradoReflector

@Composable
fun VitrinaScreen(
    catalogoMedallas: List<DefinicionMedalla>,
    medallasGanadas: Set<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.icono_vitrina),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text("Vitrina de medallas", style = MaterialTheme.typography.headlineMedium)
        }
        Text("${medallasGanadas.size} de ${catalogoMedallas.size} ganadas")
        catalogoMedallas.chunked(3).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fila.forEach { medalla ->
                    TarjetaMedalla(
                        medalla = medalla,
                        ganada = medalla.id in medallasGanadas,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaMedalla(medalla: DefinicionMedalla, ganada: Boolean, modifier: Modifier = Modifier) {
    val colorFondo = if (ganada) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)
    val descripcionAccesible = if (ganada) {
        "${medalla.nombre}, medalla ganada: ${medalla.descripcion}"
    } else {
        "${medalla.nombre}, medalla bloqueada: ${medalla.descripcion}"
    }

    Box(
        modifier = modifier
            .background(colorFondo, RoundedCornerShape(12.dp))
            .semantics { contentDescription = descripcionAccesible },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = medallaDrawable(medalla.id)),
                contentDescription = null,
                alpha = if (ganada) 1f else 0.3f,
                modifier = Modifier.size(48.dp)
            )
            Text(
                medalla.nombre,
                color = if (ganada) DoradoReflector else MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
            if (!ganada) {
                Text("Bloqueada", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}
