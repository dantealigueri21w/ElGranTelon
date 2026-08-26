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

@Composable
fun FuncionDeRepasoScreen(
    poemasDominados: List<PoemaEntity>,
    onRepasar: (PoemaEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Función de repaso", style = MaterialTheme.typography.headlineMedium)
        if (poemasDominados.isEmpty()) {
            Text("Todavía no dominas ningún poema para repasar. Vuelve cuando ganes tu primer cartel.")
        } else {
            Text("Elige un poema ya dominado y mejora tu resultado.")
            poemasDominados.forEach { poema ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
                        .clickable { onRepasar(poema) }
                        .semantics { contentDescription = "Repasar ${poema.titulo}" },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        poema.titulo,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}
