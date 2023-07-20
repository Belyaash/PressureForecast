package model

data class OpenMeteoWeather(
    val temperature: Float,
    val windspeed: Float,
    val winddirection: Float,
    val weathercode: Int,
    val is_day: Int,
    val time: String
)