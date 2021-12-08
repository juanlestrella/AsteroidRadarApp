package com.udacity.asteroidradar.database

import android.content.Context
import android.graphics.Picture
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface AsteroidsDao{

    @Query("SELECT * FROM Asteroid")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: Asteroid)

//    @Query("SELECT * FROM PictureOfDay")
//    fun getImageOfTheDay(): LiveData<PictureOfDay>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertImage(image: PictureOfDay)
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

