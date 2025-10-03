package com.example.weather.screens

import android.R.attr.padding
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.weather.models.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.data.WeatherResponse
import com.example.weather.models.WeatherUiState
import com.example.weather.screens.HourChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(model: WeatherViewModel = viewModel()) {
    val state by model.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Погода") }) }
    ) { padding ->
        Box(Modifier
            .padding(padding)
            .fillMaxSize()) {
            AnimatedContent(targetState = state, Modifier.align(Alignment.Center)) { s ->
                when (s) {
                    is WeatherUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(
                        Alignment.Center))
                    is WeatherUiState.Error -> Text("Ошибка: ${s.message}", modifier = Modifier.align(Alignment.Center))
                    is WeatherUiState.Ready -> Content(s.data)
                }
            }
        }

    }
}

@Composable
fun Content(data: WeatherResponse) {
    val current = data.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card (Modifier.fillMaxWidth()) {
            Column (Modifier.padding(20.dp)) {
                Text(
                    text = emojiForCode(current?.weatherCode) + "  " + textForCode(current?.weatherCode),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = formatTemp(current?.temperatureC),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Ветер: ${current?.windSpeedKmh?.toInt() ?: "—"} км/ч",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        val hourly = data.hourly
        if (hourly?.time != null && hourly.temperatureC != null && hourly.weatherCode != null) {
            Text("Ближайшие часы", style = MaterialTheme.typography.titleMedium)
            LazyColumn (Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(hourly.time) { index, timeStr ->
                    HourChip(
                        time = timeStr.substringAfter('T', timeStr),
                        temp = hourly.temperatureC.getOrNull(index),
                        code = hourly.weatherCode.getOrNull(index)
                    )
                }
            }
        }
    }
}

@Composable
private fun HourChip(time: String?, temp: Double?, code: Int?) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(time ?: "—", style = MaterialTheme.typography.labelMedium,)
            Spacer(Modifier.weight(1f))

            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(emojiForCode(code), fontSize = 24.sp)
                Text(formatTemp(temp), fontWeight = FontWeight.SemiBold) }
        }
    }
}

private fun formatTemp(value: Double?): String =
    if (value == null) "—" else "${value.toInt()}°"

private fun emojiForCode(code: Int?): String = when (code) {
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

private fun textForCode(code: Int?): String = when (code) {
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
