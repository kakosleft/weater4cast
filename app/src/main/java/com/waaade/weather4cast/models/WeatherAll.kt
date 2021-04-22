package com.waaade.weather4cast.models

data class WeatherAll(val dailyWeather: DailyWeather, val currentWeather: CurrentWeather, val hourlyWeather: HourlyWeather) {
}