package com.example.weather.data
object WeatherMappers {
    fun formatTemp(value: Double?): String =
        if (value == null) "—" else "${value.toInt()}°"

    fun emojiForCode(code: Int?): String = when (code) {
        0 -> "☀️"
        1, 2, 3 -> "⛅️"
        45, 48 -> "🌫️"
        in 51..57 -> "🌦️"
        in 61..67 -> "🌧️"
        in 71..77 -> "🌨️"
        in 80..82 -> "🌧️"
        in 85..86 -> "❄️"
        in 95..99 -> "⛈️"
        else -> "🌡️"
    }

    fun textForCode(code: Int?): String = when (code) {
        0 -> "Ясно"
        1 -> "Преимущественно ясно"
        2 -> "Переменная облачность"
        3 -> "Пасмурно"
        45, 48 -> "Туман / изморозь"
        51, 53, 55 -> "Морось"
        56, 57 -> "Переохлажд. морось"
        61, 63, 65 -> "Дождь"
        66, 67 -> "Переохлажд. дождь"
        71, 73, 75, 77 -> "Снег / крупа"
        80, 81, 82 -> "Ливни"
        85, 86 -> "Снегопад"
        95, 96, 99 -> "Гроза"
        else -> "Погода"
    }
}