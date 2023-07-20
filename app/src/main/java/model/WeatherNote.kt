package model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import utils.TimestampConverter
import java.util.Date

@Entity(tableName = "weather_note")
public class WeatherNote(
    @PrimaryKey(autoGenerate = true,)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "dateTime")
    val dateTime: Date,

    @ColumnInfo("weather")
    val weather: WeatherCode,

    @ColumnInfo("sys")
    var sys: Int,

    @ColumnInfo("dya")
    var dya: Int,

    @ColumnInfo("pulse")
    var pulse: Int,

    @ColumnInfo("temperatureC")
    var temperatureC: Int,
    ){
}
