package com.example.weatherapp.business.model

data class WeatherDataModel(
    val current: Current,
    val daily: List<DealyWeatherModel>,
    val hourly: List<HourlyWeatherModel>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)