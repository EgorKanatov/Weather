package com.example.weather.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.WeatherRepository
import com.example.weather.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Ready(val data: WeatherResponse) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel(
    private val repo: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state

    fun refresh() {
        _state.value = WeatherUiState.Loading
        viewModelScope.launch {
            try {
                val resp = repo.loadMoscow()
                _state.value = WeatherUiState.Ready(resp)
            } catch (t: Throwable) {
                _state.value = WeatherUiState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    init {
        refresh()
    }
}