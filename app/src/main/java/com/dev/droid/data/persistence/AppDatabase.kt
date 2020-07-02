package com.dev.droid.data.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev.droid.SampleApp
import com.dev.droid.data.network.Movie

/**
 * Created by A.K.HTOO on 02/07/2020,July,2020.
 */
@Database(entities = [Movie::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        fun create(): AppDatabase {
            return Room.databaseBuilder(
                SampleApp.appContext,
                AppDatabase::class.java, "database"
            ).build()
        }
    }
}