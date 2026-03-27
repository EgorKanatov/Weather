package com.example.weather.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import com.example.weather.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.data.Current
import com.example.weather.data.Hourly
import com.example.weather.data.WeatherMappers
import com.example.weather.data.WeatherResponse
import com.example.weather.models.WeatherUiState
import com.example.weather.models.WeatherViewModel

/**
 * Главный экран приложения погоды.
 * Отвечает за подписку на состояние ViewModel и маршрутизацию UI
 * в зависимости от текущего стейта (Загрузка / Ошибка / Данные).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(model: WeatherViewModel = viewModel()) {
    val state by model.state.collectAsState()

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Москва") },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(id = R.drawable.location_on_24px),
                            contentDescription = "Выбрать локацию"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = state,
                modifier = Modifier.align(Alignment.Center),
                label = "WeatherStateAnimation"
            ) { s ->
                when (s) {
                    is WeatherUiState.Loading -> CircularProgressIndicator()
                    is WeatherUiState.Error -> Text("Ошибка: ${s.message}")
                    is WeatherUiState.Ready -> WeatherContent(s.data)
                }
            }
        }
    }
}

/**
 * Отрисовка успешного состояния погоды.
 * Использует LazyColumn в качестве корневого элемента для поддержки
 * скролла на небольших экранах при добавлении новых блоков.
 */
@Composable
fun WeatherContent(data: WeatherResponse) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CurrentWeatherCard(data.current)
        }

        val hourly = data.hourly
        if (hourly?.time != null && hourly.temperatureC != null && hourly.weatherCode != null) {
            item {
                HourlyWeatherCard(hourly)
            }
        }
    }
}

/**
 * Блок с текущими погодными данными (температура, статус, скорость ветра).
 */
@Composable
private fun CurrentWeatherCard(current: Current?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "${WeatherMappers.emojiForCode(current?.weatherCode)}  ${
                    WeatherMappers.textForCode(current?.weatherCode)
                }",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = WeatherMappers.formatTemp(current?.temperatureC),
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
}

/**
 * Карточка, содержащая горизонтальный скроллящийся список (карусель)
 * с прогнозом погоды на ближайшие часы.
 */
@Composable
private fun HourlyWeatherCard(hourly: Hourly) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ближайшие часы",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val times = hourly.time
                val temps = hourly.temperatureC
                val codes = hourly.weatherCode

                if (times != null) {
                    itemsIndexed(times) { index, timeStr ->
                        HourItem(
                            timeStr = timeStr,
                            temp = temps?.getOrNull(index),
                            code = codes?.getOrNull(index)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Единичный элемент списка почасового прогноза.
 * * @param timeStr Ожидает время в формате ISO 8601 (например, "2024-01-01T14:00").
 * @param temp Температура в градусах Цельсия.
 * @param code WMO код состояния погоды.
 */
@Composable
private fun HourItem(timeStr: String, temp: Double?, code: Int?) {
    val formattedTime = timeStr.substringAfter('T', timeStr)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = WeatherMappers.emojiForCode(code),
            fontSize = 28.sp
        )
        Text(
            text = WeatherMappers.formatTemp(temp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}