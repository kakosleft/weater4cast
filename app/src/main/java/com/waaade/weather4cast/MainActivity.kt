/*****************************************************
 * в этом проекте для хранения api ключа используется
 * файл под названием API_KEY, если вы хотите запустить
 * этот проект то создайте API_KEY.kt и создайте в нем
 * публичную переменную key. Переменной key присвойте
 * значение вашего OpenWeather one api ключа
 ******************************************************/

package com.waaade.weather4cast

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.waaade.weather4cast.fragments.FragmentBodyDaily
import com.waaade.weather4cast.fragments.FragmentBodyHourly
import com.waaade.weather4cast.fragments.FragmentHeader
import com.waaade.weather4cast.models.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    private lateinit var buttonHourly: Button
    private lateinit var buttonDaily: Button
    private lateinit var swipper: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshScreenListener()

        initButtons()
        requestPermissionsGPS()
        getLocation()
    }

    private fun refreshScreenListener() {
        swipper = findViewById(R.id.swipeToRefresh)
        swipper.setOnRefreshListener {
            runOnline()
        }
    }

    override fun onStart() {
        super.onStart()

        if (getCache("Weather_current") != "null") {
            runWithCache()
        } else {
            runOnline()
        }
    }

    private fun runWithCache() {
        getWeatherCache()
        setHeadFragment()
        setBodyHourlyFragment()
        checkCacheUpToDate()
    }

    private fun checkCacheUpToDate(){
        println(WeatherData.current.dt)
        val newDate: Long = Date().time / 1000
        println(newDate)
        val oldDate: Long = Date(WeatherData.current.dt.toLong()).time


        val seconds: Long = (newDate - oldDate)
        println(seconds)
        if (seconds > 600){
            runOnline()
        }

    }

    private fun runOnline(){
        getLocation()
        if (locationGps != null) {
            callWeather(locationGps!!)
        } else if (locationNetwork != null) {
            callWeather(locationNetwork!!)
        }
    }

    private fun initButtons() {
        buttonDaily = findViewById(R.id.btn_daily)
        buttonHourly = findViewById(R.id.btn_hourly)

        buttonHourly.setOnClickListener {
            setBodyHourlyFragment()
        }

        buttonDaily.setOnClickListener {
            setBodyDailyFragment()
        }
    }

    private fun setHeadFragment() {
        val fragmentHeader = FragmentHeader()
        supportFragmentManager.beginTransaction().replace(R.id.header_layout, fragmentHeader).commit()
    }

    private fun setBodyHourlyFragment() {
        val fragmentBodyHourly = FragmentBodyHourly()
        supportFragmentManager.beginTransaction().replace(R.id.body_layout, fragmentBodyHourly).commit()
    }

    private fun setBodyDailyFragment() {
        val fragmentBodyDaily = FragmentBodyDaily()
        supportFragmentManager.beginTransaction().replace(R.id.body_layout, fragmentBodyDaily).commit()
    }

    private fun callWeather(location: Location) {
        val weather = Weather(
                location.latitude.toString(),
                location.longitude.toString()
        )
        weather.getWeather()
        println(
                "______________" + location.latitude.toString()
                        + "_____________" + location.longitude.toString()
        )
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        when {
            hasGps -> {
                getLocationByGPS()
            }
            hasNetwork -> {
                getLocationByNetwork()
            }
            else -> {
                locationGps = getLocationCache()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationByGPS() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                object :
                        LocationListener {
                    override fun onLocationChanged(location: Location) {}
                    override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                    ) {
                    }
                })

        val localGpsLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (localGpsLocation != null) {
            saveLocationCache(localGpsLocation)
            locationGps = localGpsLocation
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationByNetwork() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {}
                    override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                    ) {
                    }
                })

        val localNetworkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (localNetworkLocation != null) {
            saveLocationCache(localNetworkLocation)
            locationNetwork = localNetworkLocation
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsGPS() {
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    2
            )
        }
    }

    private fun setCache(key: String, value_json: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value_json)
            apply()
        }
    }

    private fun getCache(key: String): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "null").toString()
    }

    private fun saveLocationCache(location: Location) {
        setCache("Location_provider", location.provider.toString())
        setCache("Location_lat", location.latitude.toString())
        setCache("Location_lon", location.longitude.toString())
    }

    private fun getLocationCache(): Location? {
        val loc = Location(getCache("Location_provider"))
        loc.latitude = getCache("Location_lat").toDouble()
        loc.longitude = getCache("Location_lon").toDouble()
        return if (loc.provider == "null") {
            null
        } else {
            loc
        }
    }

    private fun saveWeatherCache() {
        val daily = Gson().toJson(WeatherData.daily)
        val hourly = Gson().toJson(WeatherData.hourly)
        val current = Gson().toJson(WeatherData.current)

        setCache("Weather_daily", daily)
        setCache("Weather_hourly", hourly)
        setCache("Weather_current", current)
    }

    private fun getWeatherCache(){
        val dailyCache = getCache("Weather_daily")
        val hourlyCache = getCache("Weather_hourly")
        val currenCache = getCache("Weather_current")

        val hourlyTypeToken = object : TypeToken<List<HourlyWeather>>() { }.type
        val dailyTypeToken = object : TypeToken<List<DailyWeather>>() { }.type
        val currentTypeToken = object : TypeToken<CurrentWeather>() { }.type

        WeatherData.hourly = Gson().fromJson<List<HourlyWeather>>(hourlyCache, hourlyTypeToken).toMutableList()
        WeatherData.daily = Gson().fromJson<List<DailyWeather>>(dailyCache, dailyTypeToken).toMutableList()
        WeatherData.current = Gson().fromJson<CurrentWeather>(currenCache, currentTypeToken)

        println(WeatherData.daily)
        println(WeatherData.hourly)
        println(WeatherData.current)
    }

    object WeatherData {
        var current = CurrentWeather(
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null",
                "null"
        )
        var hourly: MutableList<HourlyWeather> = ArrayList()
        var daily: MutableList<DailyWeather> = ArrayList()
        var timezone = ""
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    inner class Weather(lat: String, lon: String) {

        var okHttpClient: OkHttpClient = OkHttpClient()

        private val apiKey = API_KEY().key

        private val URL =
                "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&appid=$apiKey"

        fun getWeather() {

            println("_________$URL")
            val request: Request = Request.Builder().url(URL).build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val res = response?.body()?.string()
                    WeatherData.timezone = JSONObject(res).get("timezone").toString()
                    parseCurrent(JSONObject(res).getJSONObject("current"))
                    parseHourly(JSONObject(res).getJSONArray("hourly"))
                    parseDaily(JSONObject(res).getJSONArray("daily"))
                    runOnUiThread {
                        setHeadFragment()
                        setBodyHourlyFragment()
                        swipper.isRefreshing = false
                    }
                    saveWeatherCache()
                }

            })

        }

        fun parseCurrent(json: JSONObject) {
            val qwe = json.getJSONArray("weather")
            val asd = qwe.getJSONObject(0)

            val currentWeather = CurrentWeather(
                    dt = json.get("dt").toString(),
                    sunrise = json.get("sunrise").toString(),
                    sunset = json.get("sunset").toString(),
                    temp = parseTemp(json.get("temp").toString()),
                    feels_like = parseTemp(json.get("feels_like").toString()),
                    humidity = json.get("humidity").toString(),
                    visibility = json.get("visibility").toString(),
                    wind_speed = "${json.get("wind_speed").toString().toDouble().toInt()} km/h",
                    wind_deg = json.get("wind_deg").toString(),
                    description = asd.get("description").toString(),
                    icon = asd.get("icon").toString(),
                    uvi = json.get("uvi").toString()
            )

            WeatherData.current = currentWeather
            println(WeatherData.current)

        }

        fun parseHourly(json: JSONArray) {

            WeatherData.hourly.clear()

            for (i in 1 until 25) {

                val qwe = json.getJSONObject(i)
                val asd = qwe.getJSONArray("weather")
                val zxc = asd.getJSONObject(0)

                val wind = qwe.get("wind_speed").toString().toDouble().toInt()
                val dt = qwe.get("dt").toString()
                val temp = parseTemp(qwe.get("temp").toString())
                val feelsLike = parseTemp(qwe.get("feels_like").toString())
                val humidity = qwe.get("humidity").toString()
                val uvi = qwe.get("uvi").toString()
                val windSpeed = "$wind"
                val windDeg = qwe.get("wind_deg").toString()

                val description = zxc.get("description").toString()
                val icon = zxc.get("icon").toString()

                val sdf = SimpleDateFormat("HH:mm")

                var now = Calendar.getInstance()
                var dt_ = Calendar.getInstance()
                dt_.timeInMillis = dt.toLong() * 1000

                var day = if(now.get(Calendar.DATE) == dt_.get(Calendar.DATE)){
                    "today"
                }else{
                    "tomorrow"
                }

                val hourlyWeather = HourlyWeather(
                        day = day,
                        dt = sdf.format(dt.toLong() * 1000),
                        temp = temp,
                        feels_like = feelsLike,
                        humidity = humidity,
                        uvi = uvi,
                        wind_speed = windSpeed,
                        wind_deg = windDeg,
                        description = description,
                        icon = icon
                )
                WeatherData.hourly.add(hourlyWeather)
            }

            println(WeatherData.hourly)
        }

        private fun parseTemp(qwe: String): String {
            val tmp = qwe.toDouble().toInt()
            var t = ""
            if (tmp > 0){
                t += "+"
            }else if( tmp < 0){
                t+="-"
            }
            return "$t$tmp"
        }

        fun parseDaily(json: JSONArray) {
            WeatherData.daily.clear()

            for (i in 1 until json.length()) {

                val qwe = json.getJSONObject(i)
                val zxc = qwe.getJSONArray("weather").getJSONObject(0)

                val temp = qwe.getJSONObject("temp")
                val feelsLike = qwe.getJSONObject("feels_like")

                val sdf = SimpleDateFormat("EEEE", Locale.ENGLISH)
                val dayOfWeek = sdf.format(Date(qwe.get("dt").toString().toLong() * 1000))

                val dailyWeather = DailyWeather(
                        dt = dayOfWeek,
                        sunrise = qwe.get("sunrise").toString(),
                        sunset = qwe.get("sunset").toString(),
                        temp = Temp(
                                day = parseTemp(temp.get("day").toString()),
                                min = parseTemp(temp.get("min").toString()),
                                max = parseTemp(temp.get("max").toString()),
                                eve = parseTemp(temp.get("eve").toString()),
                                night = parseTemp(temp.get("night").toString()),
                                morn = parseTemp(temp.get("morn").toString())
                        ),
                        feels_like = FeelsLike(
                                day = parseTemp(feelsLike.get("day").toString()),
                                eve = parseTemp(feelsLike.get("eve").toString()),
                                morn = parseTemp(feelsLike.get("morn").toString()),
                                night = parseTemp(feelsLike.get("night").toString())
                        ),
                        humidity = qwe.get("humidity").toString(),
                        wind_speed = qwe.get("wind_speed").toString(),
                        wind_deg = qwe.get("wind_deg").toString(),
                        description = zxc.get("description").toString(),
                        icon = zxc.get("icon").toString(),
                        uvi = qwe.get("uvi").toString()
                )

                WeatherData.daily.add(dailyWeather)
            }
            println(WeatherData.daily)

        }


    }

}


