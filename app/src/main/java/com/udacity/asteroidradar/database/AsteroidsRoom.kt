package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao{

    @Query("SELECT * FROM AsteroidEntities")
    fun getAsteroids(): LiveData<List<Asteroid>>
}

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
}
private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context) : AsteroidsDatabase{
    return INSTANCE ?: synchronized(AsteroidsDatabase::class.java){
        val instance = Room.databaseBuilder(
            context,
            AsteroidsDatabase::class.java,
            "asteroids_database")
            .build()
        INSTANCE = instance
        instance
    }
}

