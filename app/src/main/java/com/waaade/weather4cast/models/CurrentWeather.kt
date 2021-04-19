package com.waaade.weather4cast.models

data class CurrentWeather(
    val sunrise: String,
    val sunset: String,
    val temp: String,
    val feels_like: String,
    val humidity: String,
    val clouds: String,
    val visibility: String,
    val wind_speed: String,
    val wind_deg: String,
    val main: String,
    val icon: String
) {}