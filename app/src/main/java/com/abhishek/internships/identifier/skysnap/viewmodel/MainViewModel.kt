package com.abhishek.internships.identifier.skysnap.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.internships.identifier.skysnap.model.WeatherResponse
import com.abhishek.internships.identifier.skysnap.retrofitcall.RetrofitClient
import kotlinx.coroutines.launch


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


}