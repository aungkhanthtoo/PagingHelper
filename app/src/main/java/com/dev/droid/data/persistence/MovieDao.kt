package com.dev.droid.data.persistence

import android.media.midi.MidiOutputPort
import androidx.lifecycle.LiveData
import androidx.room.*
import com.dev.droid.data.network.Movie

/**
 * Created by A.K.HTOO on 02/07/2020,July,2020.
 */
@Dao
abstract class MovieDao {

    @Query("SELECT * FROM movies")
    abstract fun getMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addAll(movie: List<Movie>)

    @Query("DELETE FROM movies")
    abstract fun deleteAll()

    @Transaction
    open fun setAll(movie: List<Movie>) {
        deleteAll()
        addAll(movie)
    }
}