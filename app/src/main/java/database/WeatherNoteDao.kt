package database

import androidx.lifecycle.LiveData
import androidx.room.*
import model.WeatherNote

@Dao
interface WeatherNoteDao {
    @Query("SELECT * FROM weather_note ORDER BY dateTime DESC")
    fun getAll(): LiveData<List<WeatherNote>>

    @Query("SELECT * FROM weather_note ORDER BY dateTime")
    fun getAllAsc(): LiveData<List<WeatherNote>>

    @Query("SELECT * FROM weather_note WHERE id = :id")
    fun getById(id: Int): WeatherNote

    @Insert
    fun insert(vararg result: WeatherNote)

    @Delete
    fun delete(result: WeatherNote)

    @Update
    fun update(vararg result: WeatherNote)
}