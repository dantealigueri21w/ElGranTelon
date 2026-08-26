package pe.appmobile.elgrantelon.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.appmobile.elgrantelon.data.dao.ActoDao
import pe.appmobile.elgrantelon.data.dao.CartelDao
import pe.appmobile.elgrantelon.data.dao.IntentoDeclamacionDao
import pe.appmobile.elgrantelon.data.dao.MedallaDao
import pe.appmobile.elgrantelon.data.dao.PerfilDao
import pe.appmobile.elgrantelon.data.dao.PoemaDao
import pe.appmobile.elgrantelon.data.dao.RachaDao
import pe.appmobile.elgrantelon.data.entity.ActoEntity
import pe.appmobile.elgrantelon.data.entity.CartelEntity
import pe.appmobile.elgrantelon.data.entity.IntentoDeclamacionEntity
import pe.appmobile.elgrantelon.data.entity.MedallaEntity
import pe.appmobile.elgrantelon.data.entity.PerfilEntity
import pe.appmobile.elgrantelon.data.entity.PoemaEntity
import pe.appmobile.elgrantelon.data.entity.RachaEntity

@Database(
    entities = [
        PerfilEntity::class, ActoEntity::class, PoemaEntity::class,
        IntentoDeclamacionEntity::class, CartelEntity::class, MedallaEntity::class, RachaEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun actoDao(): ActoDao
    abstract fun poemaDao(): PoemaDao
    abstract fun intentoDeclamacionDao(): IntentoDeclamacionDao
    abstract fun cartelDao(): CartelDao
    abstract fun medallaDao(): MedallaDao
    abstract fun rachaDao(): RachaDao

    companion object {
        @Volatile private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "el_gran_telon.db"
                ).build().also { instancia = it }
            }
    }
}
