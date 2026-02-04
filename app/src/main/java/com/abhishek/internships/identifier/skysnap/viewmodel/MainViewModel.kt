package com.abhishek.internships.identifier.skysnap.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.internships.identifier.skysnap.model.WeatherResponse
import com.abhishek.internships.identifier.skysnap.retrofitcall.RetrofitClient
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"
    private var _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData


      fun getLocationWeatherDetails(latitude: Double, longitude: Double) {

          viewModelScope.launch {
              try {
                  val weather = RetrofitClient.api.getCurrentWeather(latitude, longitude)
                  _weatherData.postValue(weather)
                  Log.d(TAG, "Weather Response: $weather")
              } catch (e: Exception) {
                  Log.e(TAG, "API Call Failed: ${e.message}")
              }
          }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(input: String): String {
        return try {
            val parsedDate = LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val formatter =
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy | HH:mm", Locale.ENGLISH)
            parsedDate.format(formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            input
        }
    }
}