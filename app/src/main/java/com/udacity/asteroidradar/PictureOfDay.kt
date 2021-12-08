package com.udacity.asteroidradar

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "pictureofday")
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)