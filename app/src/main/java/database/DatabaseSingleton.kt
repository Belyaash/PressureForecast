package database

import android.content.Context
import androidx.room.Room

object DatabaseSingleton {
    var db: AppDatabase? = null

    public fun getDB(context: Context):AppDatabase {
        if (db == null){
            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "weather.db").build()
        }

        return db as AppDatabase
    }
}