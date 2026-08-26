package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.elgrantelon.data.entity.MedallaEntity

@Dao
interface MedallaDao {
    @Query("SELECT * FROM medalla")
    suspend fun listarGanadas(): List<MedallaEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun guardar(medalla: MedallaEntity)
}
