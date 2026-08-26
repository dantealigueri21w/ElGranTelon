package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pe.appmobile.elgrantelon.data.entity.PoemaEntity

@Dao
interface PoemaDao {
    @Query("SELECT * FROM poema ORDER BY id")
    suspend fun listarTodos(): List<PoemaEntity>

    @Query("SELECT * FROM poema WHERE actoId = :actoId ORDER BY id")
    suspend fun listarPorActo(actoId: Int): List<PoemaEntity>

    @Query("SELECT * FROM poema WHERE id = :id")
    suspend fun obtenerPorId(id: Int): PoemaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(poemas: List<PoemaEntity>)

    @Update
    suspend fun actualizar(poema: PoemaEntity)
}
