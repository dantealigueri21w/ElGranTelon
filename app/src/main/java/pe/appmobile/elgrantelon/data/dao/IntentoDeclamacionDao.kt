package pe.appmobile.elgrantelon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import pe.appmobile.elgrantelon.data.entity.IntentoDeclamacionEntity

@Dao
interface IntentoDeclamacionDao {
    @Insert
    suspend fun insertar(intento: IntentoDeclamacionEntity): Long

    @Query("SELECT * FROM intento_declamacion WHERE poemaId = :poemaId ORDER BY fechaEpochDay DESC")
    suspend fun listarPorPoema(poemaId: Int): List<IntentoDeclamacionEntity>

    @Query("SELECT * FROM intento_declamacion WHERE aprobado = 0 ORDER BY fechaEpochDay DESC LIMIT :limite")
    suspend fun listarIntentosNoAprobados(limite: Int): List<IntentoDeclamacionEntity>
}
