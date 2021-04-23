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
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.waaade.weather4cast.fragments.FragmentBodyDaily
import com.waaade.weather4cast.fragments.FragmentBodyHourly
import com.waaade.weather4cast.fragments.FragmentHeader
import com.waaade.weather4cast.models.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cacheDaily = getCahe("daily")
        val cacheHourly = getCahe("hourly")
        val cacheCurrent = getCahe("current")
        requestPermissionsGPS()
        getLocation()
        if (cacheCurrent != "null" && cacheDaily != "null" && cacheHourly != "null") {

        } else {
            getLocation()
            if (locationGps != null) {
                callWeathr(locationGps!!)
            } else if (locationNetwork != null) {
                callWeathr(locationNetwork!!)
            }
        }
    }

    fun setHeadFragment() {
        val fragmentHeader = FragmentHeader()
        supportFragmentManager.beginTransaction().replace(R.id.header_layout, fragmentHeader).commit()
    }

    fun setBodyHourlyFragment(){
        val fragmentBodyHourly = FragmentBodyHourly()
        supportFragmentManager.beginTransaction().replace(R.id.body_layout, fragmentBodyHourly).commit()
    }

    fun setBodyDainlyFragment(){
        val fragmentBodyDaily = FragmentBodyDaily()
        supportFragmentManager.beginTransaction().replace(R.id.body_layout, fragmentBodyDaily).commit()
    }

    private fun callWeathr(location: Location) {
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

        if (hasGps) {
            getLocationByGPS()
        } else if (hasNetwork) {
            getLocationByNetwork()
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
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
        if (localGpsLocation != null)
            locationGps = localGpsLocation
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
        if (localNetworkLocation != null)
            locationNetwork = localNetworkLocation
    }

    private fun checkPrermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsGPS() {
        if (!checkPrermissions()) {
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

    fun setCache(key: String, value_json: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(key, value_json)
            apply()
        }
    }

    fun getCahe(key: String): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(key, "null").toString()
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
                "null"
        )
        var hourly: MutableList<HourlyWeather> = ArrayList()
        var daily: MutableList<DailyWeather> = ArrayList()
    }

    inner @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    class Weather(lat: String, lon: String) {

        var okHttpClient: OkHttpClient = OkHttpClient()
        lateinit var json: JSONObject

        val apiKey = API_KEY().key

        val URL =
                "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&units=metric&appid=$apiKey"

        fun getWeather() {


            val request: Request = Request.Builder().url(URL).build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val res = response?.body()?.string()
                    parseCurrent(JSONObject(res).getJSONObject("current"))
                    parseHourly(JSONObject(res).getJSONArray("hourly"))
                    parseDaily(JSONObject(res).getJSONArray("daily"))
                    runOnUiThread {
                        setHeadFragment()
                        setBodyHourlyFragment()
                    }
                }

            })

        }

        fun parseCurrent(json: JSONObject) {

            val qwe = json.getJSONArray("weather")
            val asd = qwe.getJSONObject(0)

            val currentWeather = CurrentWeather(
                    sunrise = json.get("sunrise").toString(),
                    sunset = json.get("sunset").toString(),
                    temp = json.get("temp").toString(),
                    feels_like = json.get("feels_like").toString(),
                    humidity = json.get("humidity").toString(),
                    visibility = json.get("visibility").toString(),
                    wind_speed = json.get("wind_speed").toString(),
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

            for (i in 0 until json.length()) {

                val qwe = json.getJSONObject(i)
                val asd = qwe.getJSONArray("weather")
                val zxc = asd.getJSONObject(0)

                val dt = qwe.get("dt").toString()
                val temp = qwe.get("temp").toString()
                val feels_like = qwe.get("feels_like").toString()
                val humidity = qwe.get("humidity").toString()
                val uvi = qwe.get("uvi").toString()
                val wind_speed = qwe.get("wind_speed").toString()
                val wind_deg = qwe.get("wind_deg").toString()

                val description = zxc.get("description").toString()
                val icon = zxc.get("icon").toString()

                val hourlyWeather = HourlyWeather(
                        dt = dt,
                        temp = temp,
                        feels_like = feels_like,
                        humidity = humidity,
                        uvi = uvi,
                        wind_speed = wind_speed,
                        wind_deg = wind_deg,
                        description = description,
                        icon = icon
                )
                WeatherData.hourly.add(hourlyWeather)
            }

            println(WeatherData.hourly)
        }

        fun parseDaily(json: JSONArray) {
            WeatherData.daily.clear()

            for (i in 0 until json.length()) {

                val qwe = json.getJSONObject(i)
                val zxc = qwe.getJSONArray("weather").getJSONObject(0)

                val temp = qwe.getJSONObject("temp")
                val feels_like = qwe.getJSONObject("feels_like")

                val dailyWeather = DailyWeather(
                        dt = qwe.get("dt").toString(),
                        sunrise = qwe.get("sunrise").toString(),
                        sunset = qwe.get("sunset").toString(),
                        temp = Temp(
                                day = temp.get("day").toString(),
                                min = temp.get("min").toString(),
                                max = temp.get("max").toString(),
                                eve = temp.get("eve").toString(),
                                night = temp.get("night").toString(),
                                morn = temp.get("morn").toString()
                        ),
                        feels_like = FeelsLike(
                                day = feels_like.get("day").toString(),
                                eve = feels_like.get("eve").toString(),
                                morn = feels_like.get("morn").toString(),
                                night = feels_like.get("night").toString()
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


//        var qwe = TEST("vasya",2,false,"qwe")
//        var gson = Gson()
//        val jsonString = gson.toJson(TEST("vasya",2,false,"qwe"))
//        println("---------"+jsonString)


