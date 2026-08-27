package pe.appmobile.elgrantelon.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.elgrantelon.data.seed.DefinicionMedalla
import pe.appmobile.elgrantelon.domain.engine.MotorEvaluacionFuncion
import pe.appmobile.elgrantelon.domain.model.ResultadoIntento
import pe.appmobile.elgrantelon.ui.theme.DoradoReflector
import pe.appmobile.elgrantelon.ui.theme.VerdeTelonLateral

@Composable
fun CaeElTelonScreen(
    resultado: ResultadoIntento,
    medallasNuevas: List<DefinicionMedalla>,
    onVolverAlTeatro: () -> Unit,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cae el telón", style = MaterialTheme.typography.headlineMedium)
        Text(MotorEvaluacionFuncion.pistaPrincipal(resultado), style = MaterialTheme.typography.titleLarge)

        FilaDato("Volumen", if (resultado.volumenAdecuado) "Firme" else "Sigue practicando")
        FilaDato("Ritmo", if (resultado.ritmoAdecuado) "Sostenido" else "Sigue practicando")
        FilaDato("Entonación", if (resultado.entonacionAdecuada) "Con variación real" else "Prueba subir y bajar más la voz")
        FilaDato("Pausas", "${resultado.pausasRespetadas} de ${resultado.pausasEsperadas}")

        Text("Tu curva de voz", style = MaterialTheme.typography.titleLarge)
        GraficoContorno(contornoTono = resultado.contornoTono)

        if (medallasNuevas.isNotEmpty()) {
            Text("Medallas nuevas", style = MaterialTheme.typography.titleLarge)
            medallasNuevas.forEach { medalla ->
                Text("★ ${medalla.nombre}: ${medalla.descripcion}", color = DoradoReflector)
            }
        }

        if (!resultado.aprobado) {
            Button(
                onClick = onReintentar,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(56.dp)
            ) {
                Text("Intentar de nuevo")
            }
        }

        Button(
            onClick = onVolverAlTeatro,
            modifier = Modifier
                .fillMaxWidth()
                .size(56.dp)
        ) {
            Text("Volver al teatro")
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Text("$etiqueta: $valor", style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun GraficoContorno(contornoTono: List<Float?>) {
    val tonosValidos = contornoTono.filterNotNull()
    val descripcion = if (tonosValidos.isEmpty()) {
        "Sin datos de entonación en este intento"
    } else {
        "Curva de entonación con ${tonosValidos.size} puntos, entre ${tonosValidos.min().toInt()} y ${tonosValidos.max().toInt()} hercios"
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .semantics { contentDescription = descripcion }
    ) {
        if (tonosValidos.size < 2) return@Canvas
        val minimo = tonosValidos.min()
        val maximo = tonosValidos.max()
        val rango = (maximo - minimo).coerceAtLeast(1f)
        val pasoX = size.width / (contornoTono.size - 1).coerceAtLeast(1)

        var puntoAnterior: Offset? = null
        contornoTono.forEachIndexed { indice, tono ->
            if (tono == null) {
                puntoAnterior = null
                return@forEachIndexed
            }
            val x = pasoX * indice
            val y = size.height - ((tono - minimo) / rango) * size.height
            val puntoActual = Offset(x, y)
            puntoAnterior?.let { anterior ->
                drawLine(color = VerdeTelonLateral, start = anterior, end = puntoActual, strokeWidth = 6f)
            }
            puntoAnterior = puntoActual
        }
    }
}
