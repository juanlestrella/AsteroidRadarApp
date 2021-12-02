package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private const val apiKey = "0yGMoxKgGXPW4ClJNyCqMEsR6eC89ZxDofuRPkwy"

/**
 * TODO
 * -connect to api and get data
 * -include recycler view
 * 0) Implement Details Screen for once an "item" is clicked
 * 1) Create a local database using Room
 * 2) Store API data to local database
 * 3) Display the asteroids from database -> Sorted by date
 * 4) Cache data of asteroid by using a worker
 *      -> downloads and saves today's asteroids in background once a day
 *      -> when device is charging and wifi is enabled
 * 5) Download "Picture of Day"
 *      -> Moshi, Picasso
 * 6) Add content description to views
 *      -> "Picture of day(Title)", details images, dialog button
 * 7) Check that app works without an internet connection
 */

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[7]

    init {
        getAsteroids()
        Log.i("MainViewModel", startDate)
        Log.i("MainViewModel", endDate)
        Log.i("MainViewModel", _status.value.toString())
        Log.i("MainViewModel", _asteroids.value.toString())
    }

    private fun getAsteroids(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = AsteroidApi.retrofitService.getAsteroids(startDate, endDate, apiKey)
                val jsonObject = JSONObject(response)
                _asteroids.postValue(parseAsteroidsJsonResult(jsonObject))
                _status.postValue("Connected")
            }catch (e: Exception) {
                _status.postValue("Failure:" + e.message)
            }
        }
    }
}