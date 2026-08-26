package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pe.appmobile.elgrantelon.data.entity.ActoEntity

@Dao
interface ActoDao {
    @Query("SELECT * FROM acto ORDER BY orden")
    suspend fun listarTodos(): List<ActoEntity>

    @Query("SELECT * FROM acto WHERE id = :id")
    suspend fun obtenerPorId(id: Int): ActoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(actos: List<ActoEntity>)

    @Update
    suspend fun actualizar(acto: ActoEntity)
}
