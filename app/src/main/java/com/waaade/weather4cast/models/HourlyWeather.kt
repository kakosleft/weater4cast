package com.waaade.weather4cast.models

data class HourlyWeather(
    val dt: String,
    val temp: String,
    val feels_like: String,
    val humidity: String,
    val uvi: String,
    val wind_speed: String,
    val wind_deg: String,
    val description: String,
    val icon: String
)