package com.abhishek.internships.identifier.skysnap.retrofitcall

import com.abhishek.internships.identifier.skysnap.model.Weather
import com.abhishek.internships.identifier.skysnap.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {


    @GET("2.5/weather?")
    fun getWeatherDetails(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<WeatherResponse>
}