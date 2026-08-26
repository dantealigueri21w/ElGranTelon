package pe.appmobile.elgrantelon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medalla")
data class MedallaEntity(
    @PrimaryKey val id: String,
    val fechaObtencionEpochDay: Long
)
