package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.elgrantelon.data.entity.PerfilEntity

@Dao
interface PerfilDao {
    @Query("SELECT * FROM perfil WHERE id = 1")
    suspend fun obtener(): PerfilEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(perfil: PerfilEntity)
}
