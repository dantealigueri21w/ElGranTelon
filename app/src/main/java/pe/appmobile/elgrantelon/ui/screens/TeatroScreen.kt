package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.R
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.ui.actoFondoDrawable
import pe.appmobile.elgrantelon.ui.components.Bemo
import pe.appmobile.elgrantelon.ui.theme.CortinaRoja
import pe.appmobile.elgrantelon.ui.theme.VerdeTelonLateral

@Composable
fun TeatroScreen(
    actos: List<ActoEntity>,
    onAbrirActo: (Int) -> Unit,
    onAbrirCamerino: () -> Unit,
    onAbrirCartelera: () -> Unit,
    onAbrirVitrina: () -> Unit,
    onAbrirAjustes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onAbrirCamerino, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.icono_camerino),
                    contentDescription = "Camerino",
                    modifier = Modifier.size(36.dp)
                )
            }
            Text("El Gran Telón", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onAbrirAjustes, modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.icono_ajustes),
                    contentDescription = "Ajustes",
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Bemo(nivelVolumen = null, tonoHz = null)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            actos.sortedBy { it.orden }.forEach { acto ->
                TarjetaActo(acto = acto, onAbrir = { onAbrirActo(acto.id) })
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable { onAbrirCartelera() }
                    .semantics { contentDescription = "Abrir la Cartelera de funciones" }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_cartelera),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text("Cartelera")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable { onAbrirVitrina() }
                    .semantics { contentDescription = "Abrir la Vitrina de medallas" }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_vitrina),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text("Medallas")
            }
        }
    }
}

@Composable
private fun TarjetaActo(acto: ActoEntity, onAbrir: () -> Unit) {
    val colorFondo = when {
        acto.completado -> VerdeTelonLateral
        acto.desbloqueado -> CortinaRoja
        else -> MaterialTheme.colorScheme.secondary
    }
    val estado = when {
        acto.completado -> "completado"
        acto.desbloqueado -> "disponible"
        else -> "bloqueado"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (acto.desbloqueado) Modifier.clickable { onAbrir() } else Modifier
            )
            .semantics { contentDescription = "${acto.nombre}, $estado" },
        contentAlignment = Alignment.CenterStart
    ) {
        Image(
            painter = painterResource(id = actoFondoDrawable(acto.orden)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo.copy(alpha = if (acto.desbloqueado) 0.55f else 0.75f))
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Text(acto.nombre, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleLarge)
            Text(
                text = when {
                    acto.completado -> "¡Función completa!"
                    acto.desbloqueado -> "Toca para entrar"
                    else -> "Bloqueado"
                },
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
