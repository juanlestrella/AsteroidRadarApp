package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[7]
    private val apiKey = "0yGMoxKgGXPW4ClJNyCqMEsR6eC89ZxDofuRPkwy"

    val asteroids: LiveData<List<Asteroid>> = database.asteroidsDao.getAsteroids()

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    suspend fun getImageOfTheDay(){
        withContext(Dispatchers.IO){
            _imageOfTheDay.value = AsteroidApi.retrofitService.getImageOfTheDay(apiKey)
        }
    }

    suspend fun refreshData() {
        withContext(Dispatchers.IO){
            val response = AsteroidApi.retrofitService.getAsteroids(startDate, endDate, apiKey)
            val jsonObject = JSONObject(response)
            val asteroidsList = parseAsteroidsJsonResult(jsonObject)
            asteroidsList.forEach{
                asteroid -> database.asteroidsDao.insert(asteroid)
            }
        }
    }
}