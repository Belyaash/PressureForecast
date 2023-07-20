package utils

import com.example.pressureforecast.R
import model.WeatherCode

object ImageGetter{

    fun getWeatherImage(code: WeatherCode):Int{
        when(code){
            WeatherCode.Sunny -> return R.mipmap.sunny
            WeatherCode.LightRain -> return R.mipmap.rainy
            WeatherCode.HeavyRain -> return R.mipmap.rainy2
            WeatherCode.Clouds -> return R.mipmap.cloudy2
            WeatherCode.Snowfall -> return R.mipmap.snowy
        }
    }
}