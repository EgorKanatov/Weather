package com.example.weather.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi{
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") timezone: String = "Europe/Moscow",
        @Query("current") current: String = "temperature_2m,weather_code,wind_speed_10m,is_day",
        @Query("hourly") hourly: String = "temperature_2m,weather_code,precipitation_probability",
        @Query("forecast_days") days: Int = 1
    ): WeatherResponse
}