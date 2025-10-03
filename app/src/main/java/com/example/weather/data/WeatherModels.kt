package com.example.weather.data

import com.squareup.moshi.Json

data class WeatherResponse(
    val timeZone: String?,
    @Json(name = "utc_offset_seconds") val utcOffsetSeconds: Int?,
    val current: Current?,
    val hourly: Hourly?)

data class Current(
    val time: String?,
    @Json(name = "temperature_2m") val temperatureC: Double?,
    @Json(name = "weather_code") val weatherCode: Int?,
    @Json(name = "wind_speed_10m") val windSpeedKmh: Double?,
    @Json(name = "is_day") val isDay: Int?
)

data class Hourly(
    val time: List<String>?,
    @Json(name = "temperature_2m") val temperatureC: List<Double>?,
    @Json(name = "weather_code") val weatherCode: List<Int>?,
    @Json(name = "precipitation_probability") val precipProbPct: List<Int>?
)