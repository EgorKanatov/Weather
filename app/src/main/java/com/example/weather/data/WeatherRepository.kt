package com.example.weather.data

class WeatherRepository(private val api: WeatherApi = NetworkModule.weatherApi) {
    private val moscowLat = 55.7558
    private val moscowLon = 37.6173

    suspend fun loadMoscow(): WeatherResponse =
        api.getForecast(lat = moscowLat, lon = moscowLon)
}