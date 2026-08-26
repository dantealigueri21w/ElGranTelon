package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.domain.engine.LecturaEnVivo
import pe.appmobile.elgrantelon.ui.components.Bemo
import pe.appmobile.elgrantelon.ui.theme.CortinaRoja
import pe.appmobile.elgrantelon.ui.theme.VerdeTelonLateral

@Composable
fun EscenarioScreen(
    poema: PoemaEntity,
    grabando: Boolean,
    lecturaEnVivo: LecturaEnVivo?,
    sinPermiso: Boolean,
    onEmpezar: () -> Unit,
    onTerminar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            poema.titulo,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleLarge
        )

        Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
            Bemo(nivelVolumen = lecturaEnVivo?.nivelVolumen, tonoHz = lecturaEnVivo?.tonoHz)
        }

        if (sinPermiso) {
            Text(
                "Bemo necesita escuchar tu voz para brillar. Activa el micrófono en los ajustes del teléfono para declamar.",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        BotonDeclamar(
            grabando = grabando,
            habilitado = !sinPermiso,
            onEmpezar = onEmpezar,
            onTerminar = onTerminar
        )
    }
}

@Composable
private fun BotonDeclamar(
    grabando: Boolean,
    habilitado: Boolean,
    onEmpezar: () -> Unit,
    onTerminar: () -> Unit
) {
    val color = if (grabando) CortinaRoja else VerdeTelonLateral
    val etiqueta = if (grabando) "Terminar" else "Empezar"

    Box(
        modifier = Modifier
            .size(140.dp)
            .clickable(enabled = habilitado) { if (grabando) onTerminar() else onEmpezar() }
            .background(color, CircleShape)
            .semantics { contentDescription = "$etiqueta a declamar" },
        contentAlignment = Alignment.Center
    ) {
        Text(etiqueta, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.titleLarge)
    }
}
