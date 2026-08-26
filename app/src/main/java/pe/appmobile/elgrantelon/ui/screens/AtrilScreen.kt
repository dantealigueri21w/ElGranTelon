package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.ui.theme.VerdeTelonLateral

@Composable
fun AtrilScreen(
    poema: PoemaEntity,
    onEmpezarADeclamar: () -> Unit
) {
    val lineas = poema.texto.split("\n")
    val marcasPausa = poema.marcasPausaCsv.split(",").filter { it.isNotBlank() }.map { it.toInt() }.toSet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(poema.titulo, style = MaterialTheme.typography.headlineMedium)
        Text(poema.autor, style = MaterialTheme.typography.bodyMedium)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            lineas.forEachIndexed { indice, linea ->
                Text(linea, style = MaterialTheme.typography.bodyLarge)
                if (indice in marcasPausa) {
                    Text(
                        "· pausa ·",
                        color = VerdeTelonLateral,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Button(
            onClick = onEmpezarADeclamar,
            modifier = Modifier
                .fillMaxWidth()
                .size(56.dp)
        ) {
            Text("Empezar a declamar")
        }
    }
}
