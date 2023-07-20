package network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import model.OpenMeteoWeather
import org.json.JSONObject
import java.net.URL

class OpenMeteoWeatherInteractor {
    val networkClient = NetworkClient()

    suspend fun requestWeather(latitute: Double, longitute:Double): OpenMeteoWeather {
        Log.e("Interactor", "requestStarted")
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitute&longitude=$longitute&current_weather=true"

        val apiResponse = URL(url).readText()
        return parseJSON(apiResponse)

    }

    private fun parseJSON(jsonString: String): OpenMeteoWeather {
        try {
            val gson = Gson()
            var JSON_Weather = JSONObject(jsonString).getJSONObject("current_weather")
            Log.e("Interactor", JSON_Weather.toString())
            return gson.fromJson(JSON_Weather.toString(), OpenMeteoWeather::class.java)
        } catch (e: Exception) {
            Log.e("Interactor", "error", e)
            return OpenMeteoWeather(0f,0f,0f,0,0,"")
        }
    }
}