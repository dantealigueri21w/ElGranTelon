package pe.appmobile.elgrantelon.domain.model

enum class NivelVolumen { BAJO, ADECUADO, ALTO }

data class LecturaVolumen(
    val rms: Float,
    val nivel: NivelVolumen
)

data class SegmentoSilencio(
    val indiceInicio: Int,
    val duracionMs: Long
)

data class ResultadoPausas(
    val pausasDetectadas: Int,
    val pausasEsperadas: Int,
    val cumplidas: Boolean
)

data class ResultadoIntento(
    val volumenPromedio: Float,
    val volumenAdecuado: Boolean,
    val contornoTono: List<Float?>,
    val entonacionAdecuada: Boolean,
    val silabasPorMinuto: Int,
    val ritmoAdecuado: Boolean,
    val pausasRespetadas: Int,
    val pausasEsperadas: Int,
    val aprobado: Boolean
)
