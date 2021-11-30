package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _asteroids = MutableLiveData<Int>()
    val asteroids: LiveData<Int>
        get() = _asteroids

    private val startDate = getNextSevenDaysFormattedDates()[0]
    private val endDate = getNextSevenDaysFormattedDates()[7]

    init {
        getAsteroids()
        Log.i("MainViewModel", startDate)
        Log.i("MainViewModel", endDate)
        Log.i("MainViewModel", _status.value.toString())
    }

    private fun getAsteroids(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = AsteroidApi.retrofitService.getAsteroids(startDate, endDate, apiKey)
                val jsonObject = JSONObject(response)
                _asteroids.postValue(parseAsteroidsJsonResult(jsonObject).size)
                _status.postValue("Connected")
            }catch (e: Exception) {
                _status.postValue("Failure:" + e.message)
            }
        }
    }
}