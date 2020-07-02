package com.dev.droid.data

import androidx.lifecycle.map
import com.dev.droid.data.executor.AppTaskExecutor
import com.dev.droid.data.network.Movie
import com.dev.droid.data.network.MoviesResponse
import com.dev.droid.data.network.livedatacalladapter.LiveDataResponse
import com.dev.droid.data.network.livedatacalladapter.ResponseLiveData
import com.dev.droid.data.persistence.AppDatabase

/**
 * Created by A.K.HTOO on 02/07/2020,July,2020.
 */
object MovieLocalDataSource : MovieRepository {

    private val dao = AppDatabase.create().movieDao()
    private val executor = AppTaskExecutor.getInstance()

    override fun getPopularMovies(page: Int): ResponseLiveData<MoviesResponse> {
        return dao.getMovies().map { LiveDataResponse(MoviesResponse(it), null) }
    }

    fun addAll(movie: List<Movie>) {
        executor.executeOnDiskIO {
            dao.addAll(movie)
        }
    }

    fun setAll(movie: List<Movie>) {
        executor.executeOnDiskIO {
            dao.setAll(movie)
        }
    }

}