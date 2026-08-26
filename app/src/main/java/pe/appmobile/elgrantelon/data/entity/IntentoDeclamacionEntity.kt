package pe.appmobile.elgrantelon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intento_declamacion")
data class IntentoDeclamacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val poemaId: Int,
    val fechaEpochDay: Long,
    val volumenPromedio: Float,
    val volumenAdecuado: Boolean,
    val entonacionAdecuada: Boolean,
    val silabasPorMinuto: Int,
    val ritmoAdecuado: Boolean,
    val pausasRespetadas: Int,
    val pausasEsperadas: Int,
    val aprobado: Boolean,
    val contornoTonoCsv: String,
    val esRepaso: Boolean = false
)
