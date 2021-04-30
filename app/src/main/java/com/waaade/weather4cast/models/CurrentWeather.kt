package com.waaade.weather4cast.models

data class CurrentWeather(
        val dt: String,
        val sunrise: String,
        val sunset: String,
        val temp: String,
        val feels_like: String,
        val humidity: String,
        val visibility: String,
        val wind_speed: String,
        val wind_deg: String,
        val description: String,
        val icon: String,
        val uvi: String
)