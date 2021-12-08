package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao{

    @Query("SELECT * FROM Asteroid")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: Asteroid)
}

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
}

private lateinit var INSTANCE: AsteroidsDatabase
fun getDatabase(context: Context) : AsteroidsDatabase{
    synchronized(AsteroidsDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context,
                AsteroidsDatabase::class.java,
                "asteroids_database")
                .build()
        }
        return INSTANCE
    }
}

