package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
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
    private val endDate = getNextSevenDaysFormattedDates()[getNextSevenDaysFormattedDates().lastIndex]

    val asteroids: LiveData<List<Asteroid>> = database.asteroidsDao.getAsteroids()

    private val _imageOfTheDay = MutableLiveData<PictureOfDay>()
    val imageOfTheDay: LiveData<PictureOfDay>
        get() = _imageOfTheDay

    suspend fun getImageOfTheDay(){
        withContext(Dispatchers.IO){
            _imageOfTheDay.postValue(AsteroidApi.retrofitService.getImageOfTheDay(BuildConfig.NASA_API_KEY))
            if (_imageOfTheDay.value!!.mediaType != "image") {
                _imageOfTheDay.postValue(null)
            }
        }
    }

    suspend fun refreshData() {
        withContext(Dispatchers.IO){
            val response = AsteroidApi.retrofitService.getAsteroids(startDate, endDate, BuildConfig.NASA_API_KEY)
            val jsonObject = JSONObject(response)
            val asteroidsList = parseAsteroidsJsonResult(jsonObject)
            asteroidsList.forEach{
                asteroid -> database.asteroidsDao.insert(asteroid)
            }
        }
    }
}