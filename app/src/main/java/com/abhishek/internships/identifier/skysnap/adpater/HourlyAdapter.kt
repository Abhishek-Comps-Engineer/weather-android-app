package com.abhishek.internships.identifier.skysnap.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhishek.internships.identifier.skysnap.R
import com.abhishek.internships.identifier.skysnap.databinding.ItemHourlyBinding
import com.abhishek.internships.identifier.skysnap.model.HourlyUnits
import com.abhishek.internships.identifier.skysnap.model.WeatherResponse
import java.util.Locale

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>() {

    private var listTime: MutableList<String>? = null
    private var listTemperature: MutableList<Double>? = null
    private var hourlyUnits: HourlyUnits? = null

    private val images = listOf(
        R.drawable.ic_wind,
        R.drawable.app_logo,
        R.drawable.ic_rain,
        R.drawable.sun_cloud,
        R.drawable.ic_humidity
    )

    fun setData(weatherResponse: WeatherResponse) {
        listTime?.clear()
        listTemperature?.clear()
        listTime = weatherResponse.hourly?.time!! as MutableList<String>?
        listTemperature = weatherResponse.hourly.temperature_2m!! as MutableList<Double>?
        hourlyUnits = weatherResponse.hourly_units
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = ItemHourlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val time = listTime?.getOrNull(position) ?: ""
        val temperature :Double = (listTemperature?.get(position) ?: 0.0 )

        // Time with units
        holder.binding.textTime.text = buildString {
            append(formateTime(time))
        }

        // Temperature with unit
        holder.binding.textTemeperature.text = buildString {
            append(formatTemperature(temperature))
            append(hourlyUnits?.temperature_2m ?: "")
        }

        // Random image per item
        val randomImage = images.random()
        holder.binding.imgWeather.setImageResource(randomImage)
    }

    private fun formateTime(time: String): String? {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = parser.parse(time)
            val formatter = SimpleDateFormat("H:mm", Locale.getDefault())
            formatter.format(date!!)
        } catch (e: Exception) {
            time // fallback if parsing fails
        }
    }

    fun formatTemperature(temp: Double?): String {
        return temp?.toInt().toString()
    }


    override fun getItemCount(): Int = listTime?.size ?: 0


    class HourlyViewHolder(val binding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root)
}
