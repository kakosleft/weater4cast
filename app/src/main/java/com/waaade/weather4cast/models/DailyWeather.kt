package com.waaade.weather4cast.models

data class DailyWeather(
    val dt: String,
    val sunrise: String,
    val sunset: String,
    val temp: Temp,
    val feels_like: FeelsLike,
    val humidity: String,
    val wind_speed: String,
    val wind_deg: String,
    val description: String,
    val icon: String,
    val uvi: String
)