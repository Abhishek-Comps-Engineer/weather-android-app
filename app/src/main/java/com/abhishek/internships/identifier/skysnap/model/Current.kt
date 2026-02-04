package com.abhishek.internships.identifier.skysnap.model

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val time: String? = null,
    val interval: Int? = null,
    val temperature_2m: Double? = null,
    val relative_humidity_2m: Int? = null,
    val wind_speed_10m: Double? = null,
    val cloud_cover: Int? = null
)