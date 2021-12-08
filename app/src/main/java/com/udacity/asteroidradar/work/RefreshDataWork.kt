package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase

//class RefreshDataWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
//    override suspend fun doWork(): Result {
//        val getDatabase = getDatabase(applicationContext)
//    }
//}