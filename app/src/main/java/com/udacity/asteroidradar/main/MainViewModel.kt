package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidApi
import kotlinx.coroutines.launch
import java.util.*

private const val apiKey = "0yGMoxKgGXPW4ClJNyCqMEsR6eC89ZxDofuRPkwy"

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _asteroids = MutableLiveData<Int>()
    val asteroids: LiveData<Int>
        get() = _asteroids

    init {

    }

    private fun getAsteroids(){
        viewModelScope.launch {
            try{

            }catch (e: Exception) {

            }
        }
    }
}