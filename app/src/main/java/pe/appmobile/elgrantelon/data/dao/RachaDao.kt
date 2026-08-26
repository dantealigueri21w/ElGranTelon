package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.elgrantelon.data.entity.RachaEntity

@Dao
interface RachaDao {
    @Query("SELECT * FROM racha WHERE id = 1")
    suspend fun obtener(): RachaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(racha: RachaEntity)
}
