//в этом проекте для хранения api ключа используется
//файл под названием API_KEY, если вы хотите запустить
//этот проект то создайте API_KEY.kt и создайте в нем
//публичную переменную key. Переменной key присвойте
//значение вашего OpenWeather one api ключа

package com.waaade.weather4cast

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.Manifest

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    val apiKey = API_KEY().key
    val URL =
        "https://api.openweathermap.org/data/2.5/onecall?lat" +
                "=42.4714808&lon=59.53379&lang=ru&units=metric&appid=" + apiKey
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getWeather()
        getLocation()

//        var qwe = TEST("vasya",2,false,"qwe")
//        var gson = Gson()
//        val jsonString = gson.toJson(TEST("vasya",2,false,"qwe"))
//        println("---------"+jsonString)


    }


    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(
            this,
            "Latitude: " + location.latitude + " , Longitude: " + location.longitude,
            Toast.LENGTH_LONG
        ).show()
        println("Latitude: " + location.latitude + " , Longitude: " + location.longitude)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getWeather() {

        val request: Request = Request.Builder().url(URL).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
//                val txt = (JSONObject(json).getJSONObject("value").get("joke")).toString()
//                println(json)
                Weather().parseCurrent(JSONObject(json).getJSONObject("current"))

            }
        })

    }


}