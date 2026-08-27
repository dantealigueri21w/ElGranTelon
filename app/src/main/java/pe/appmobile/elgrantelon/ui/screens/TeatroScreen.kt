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
    onAbrirAjustes: () -> Unit,
    onAbrirFuncionDeRepaso: () -> Unit
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
                .weight(1f)
                .fillMaxWidth()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable { onAbrirFuncionDeRepaso() }
                    .semantics { contentDescription = "Abrir la Función de Repaso" }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_repaso),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Text("Repasar")
            }
        }
    }
}

@Composable
private fun TarjetaActo(acto: ActoEntity, onAbrir: () -> Unit) {
    // Todo Acto se toca y se juega desde el primer minuto (seccion 5.1): la
    // progresion es guia (orden, dificultad), nunca candado de acceso. Los
    // unicos estados de la tarjeta son completado/disponible.
    val colorFondo = if (acto.completado) VerdeTelonLateral else CortinaRoja
    val estado = if (acto.completado) "completado" else "disponible"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onAbrir() }
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
                .background(colorFondo.copy(alpha = 0.55f))
        )
        Column(modifier = Modifier.padding(20.dp)) {
            Text(acto.nombre, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleLarge)
            Text(
                text = if (acto.completado) "¡Función completa!" else "Toca para entrar",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
