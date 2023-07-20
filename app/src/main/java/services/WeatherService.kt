package services

import android.Manifest
import android.app.Service
import com.google.gson.Gson
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Path.Op
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.OpenMeteoWeather
import network.OpenMeteoWeatherInteractor

class WeatherService : Service(), LocationListener {
    private val myBinder = null
    lateinit var mLocationManager:LocationManager


    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("WeatherService", "WeatherService Started")
        mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val locationProvider = LocationManager.NETWORK_PROVIDER
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)){
            mLocationManager.requestLocationUpdates(locationProvider, 10000, 0f, this)
            }
        return START_STICKY
    }

    override fun onLocationChanged(location: Location) {
        GlobalScope.launch {
            var x: OpenMeteoWeather? = null
            GlobalScope.async {
                x = OpenMeteoWeatherInteractor().requestWeather(location.latitude, location.longitude)
                Log.e("WeatherService", Gson().toJson(x))
            }.await()
            weather.postValue(x!!)
        }
    }

    companion object {
        var weather: MutableLiveData<OpenMeteoWeather> = MutableLiveData<OpenMeteoWeather>()
    }
}