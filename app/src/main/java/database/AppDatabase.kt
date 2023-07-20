package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import model.WeatherNote
import utils.TimestampConverter

@Database(entities = arrayOf(WeatherNote::class), version = 1)
@TypeConverters(TimestampConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherNoteDao(): WeatherNoteDao
}