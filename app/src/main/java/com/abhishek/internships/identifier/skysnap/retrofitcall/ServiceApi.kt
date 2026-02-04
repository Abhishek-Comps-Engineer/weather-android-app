package com.abhishek.internships.identifier.skysnap.retrofitcall

import com.abhishek.internships.identifier.skysnap.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ServiceApi {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,wind_speed_10m,cloud_cover"
    ): WeatherResponse
}
