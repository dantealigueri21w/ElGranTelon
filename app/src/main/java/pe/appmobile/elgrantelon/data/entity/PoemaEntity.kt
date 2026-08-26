package pe.appmobile.elgrantelon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poema")
data class PoemaEntity(
    @PrimaryKey val id: Int,
    val actoId: Int,
    val titulo: String,
    val autor: String,
    val texto: String,
    val marcasPausaCsv: String,
    val volumenMinimo: Float,
    val volumenMaximo: Float,
    val ritmoMinimoSilabasPorMinuto: Int,
    val ritmoMaximoSilabasPorMinuto: Int,
    val dominado: Boolean = false
)
