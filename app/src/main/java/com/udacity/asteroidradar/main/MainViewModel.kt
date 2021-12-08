package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch


/**
 * TODO
 * -connect to api and get data
 * -include recycler view
 * - Implement Details Screen once an "item" is clicked
 * - Create a local database using Room
 * - Store API data to local database
 * - Display the asteroids from database -> Sorted by date
 * - Cache data of asteroid by using a worker
 *      -> downloads and saves today's asteroids in background once a day
 *      -> when device is charging and wifi is enabled
 * - Download "Picture of Day"
 *      -> Moshi, Picasso
 * - Add content description to views
 *      -> "Picture of day(Title)", details images, dialog button
 * - Check that app works without an internet connection
 */

class MainViewModel(application: Application) : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            try {
                repository.refreshData()
                repository.getImageOfTheDay()
                _status.postValue("Connected")
            } catch (e: Exception) {
                _status.postValue("Failure:" + e.message)
            }
        }
    }

    val asteroids = repository.asteroids
    val imageOfTheDay = repository.imageOfTheDay //fix picasso

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

/**
 * problem: return value of parseAsteroidsJsonResult(jsonObject) is ArrayList<Asteroid>
 * possible solution:
 * still store in _asteroids.postValue the api data
 * then loop through the _asteroids.value and store the inner value in the database
 * then replace the data binding from using the _asteroids value to using the database
 *
 * Use two functions:
 * ----- 1) getApiData() = gets the data from api
 * ----- 2) loadDatabase() = store the data from api to database
 */
/*
private val asteroidDao = getDatabase(application).asteroidsDao
private fun loadDatabase(asteroidsData: LiveData<List<Asteroid>>){
    asteroidDao.insertAll(asteroidsData)
}


private val _status = MutableLiveData<String>()
val status: LiveData<String>
    get() = _status

private val _asteroids = MutableLiveData<List<Asteroid>>()
val asteroids: LiveData<List<Asteroid>>
    get() = _asteroids

private val startDate = getNextSevenDaysFormattedDates()[0]
private val endDate = getNextSevenDaysFormattedDates()[7]
getAsteroids()
Log.i("MainViewModel", startDate)
Log.i("MainViewModel", endDate)
Log.i("MainViewModel", _status.value.toString())
Log.i("MainViewModel", _asteroids.value.toString())



private fun getAsteroids(){
    viewModelScope.launch(Dispatchers.IO) {
        try{
            val response = AsteroidApi.retrofitService.getAsteroids(startDate, endDate, apiKey)
            val jsonObject = JSONObject(response)
            //database.insertAll(parseAsteroidsJsonResult(jsonObject))
            _asteroids.postValue(parseAsteroidsJsonResult(jsonObject))
            _status.postValue("Connected")
        }catch (e: Exception) {
            _status.postValue("Failure:" + e.message)
        }
    }
}*/