package pe.appmobile.elgrantelon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartel")
data class CartelEntity(
    @PrimaryKey val poemaId: Int,
    val fechaObtencionEpochDay: Long,
    val descripcionLogro: String
)
