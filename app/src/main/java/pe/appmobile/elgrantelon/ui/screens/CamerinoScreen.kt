package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.data.seed.DefinicionAvatar
import pe.appmobile.elgrantelon.ui.avatarDrawable
import pe.appmobile.elgrantelon.ui.theme.DoradoReflector

@Composable
fun CamerinoScreen(
    avatares: List<DefinicionAvatar>,
    onConfirmar: (alias: String, avatarId: Int) -> Unit
) {
    var alias by remember { mutableStateOf("") }
    var avatarSeleccionado by remember { mutableIntStateOf(avatares.firstOrNull()?.id ?: 1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("El Camerino", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Elige cómo te van a llamar en el teatro. Nunca tu nombre real.",
            style = MaterialTheme.typography.bodyMedium
        )
        OutlinedTextField(
            value = alias,
            onValueChange = { if (it.length <= 16) alias = it },
            label = { Text("Tu alias de teatro") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        Text("Elige tu avatar", style = MaterialTheme.typography.titleLarge)
        AvataresGrid(
            avatares = avatares,
            avatarSeleccionado = avatarSeleccionado,
            onSeleccionar = { avatarSeleccionado = it }
        )
        Button(
            onClick = { onConfirmar(alias.ifBlank { "Actor misterioso" }, avatarSeleccionado) },
            modifier = Modifier
                .fillMaxWidth()
                .size(56.dp)
        ) {
            Text("Entrar al teatro")
        }
    }
}

@Composable
private fun AvataresGrid(
    avatares: List<DefinicionAvatar>,
    avatarSeleccionado: Int,
    onSeleccionar: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        avatares.chunked(4).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                fila.forEach { avatar ->
                    val estaSeleccionado = avatar.id == avatarSeleccionado
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { onSeleccionar(avatar.id) }
                            .background(MaterialTheme.colorScheme.secondary, CircleShape)
                            .border(
                                width = if (estaSeleccionado) 4.dp else 0.dp,
                                color = DoradoReflector,
                                shape = CircleShape
                            )
                            .semantics { contentDescription = "Avatar ${avatar.id}" },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = avatarDrawable(avatar.id)),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    }
}
