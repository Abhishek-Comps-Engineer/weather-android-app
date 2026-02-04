package com.abhishek.internships.identifier.skysnap.model
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUnits(
    val time: String? = null,
    val interval: String? = null,
    val temperature_2m: String? = null,
    val relative_humidity_2m: String? = null,
    val wind_speed_10m: String? = null,
    val surface_pressure: String? = null
)