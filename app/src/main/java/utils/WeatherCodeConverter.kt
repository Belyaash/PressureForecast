package utils

import model.WeatherCode

object WeatherCodeConverter {

    fun OpenMeteoConvert(code:Int): WeatherCode{
        when(code){
            0,1,2,3 -> return WeatherCode.Sunny
            45,48 -> return WeatherCode.Clouds
            51, 53, 55 -> return WeatherCode.LightRain
            56, 57 -> return WeatherCode.LightRain
            61, 63, 65 -> return WeatherCode.HeavyRain
            66, 67 -> return WeatherCode.HeavyRain
            71, 73, 75 -> return WeatherCode.Snowfall
            77 -> return WeatherCode.Snowfall
            80, 81, 82 -> return WeatherCode.HeavyRain
            85, 86 -> return WeatherCode.Snowfall
            95, 96, 99 -> return WeatherCode.HeavyRain
        }
        return WeatherCode.Sunny
    }
}