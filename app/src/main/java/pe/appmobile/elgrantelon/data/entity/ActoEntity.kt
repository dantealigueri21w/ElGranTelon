package pe.appmobile.elgrantelon.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "acto")
data class ActoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val orden: Int,
    val desbloqueado: Boolean,
    val completado: Boolean = false
)
