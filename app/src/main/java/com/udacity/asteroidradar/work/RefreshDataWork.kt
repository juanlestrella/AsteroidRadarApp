package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    override suspend fun doWork(): Result {
        val getDatabase = getDatabase(applicationContext)
        val repository = AsteroidsRepository(getDatabase)

        return try{
            repository.refreshData()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}