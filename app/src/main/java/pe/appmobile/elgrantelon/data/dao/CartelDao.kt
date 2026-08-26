package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.elgrantelon.data.entity.CartelEntity

@Dao
interface CartelDao {
    @Query("SELECT * FROM cartel ORDER BY fechaObtencionEpochDay DESC")
    suspend fun listarTodos(): List<CartelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(cartel: CartelEntity)
}
