package com.abhishek.internships.identifier.skysnap.model

import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val generationtime_ms: Double? = null,
    val utc_offset_seconds: Int? = null,
    val timezone: String? = null,
    val timezone_abbreviation: String? = null,
    val elevation: Int? = null,
    val current_units: CurrentUnits? = null,
    val current: Current? = null
)



