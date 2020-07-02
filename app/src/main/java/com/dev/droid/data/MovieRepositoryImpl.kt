package com.dev.droid.data

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.dev.droid.data.network.MoviesResponse
import com.dev.droid.data.network.livedatacalladapter.LiveDataResponse
import com.dev.droid.data.network.livedatacalladapter.ResponseLiveData

/**
 * Created with love by A.K.HTOO on 02/07/2020,July,2020.
 */
object MovieRepositoryImpl: MovieRepository {

    private val local = MovieLocalDataSource
    private val remote = MovieRemoteDataSource

    override fun getPopularMovies(page: Int): ResponseLiveData<MoviesResponse> {
        var response: LiveDataResponse<MoviesResponse>? = null
        val mediator = MediatorLiveData<LiveDataResponse<MoviesResponse>>()

        val remote = remote.getPopularMovies(page)
        remote.observeForever(object : Observer<LiveDataResponse<MoviesResponse>> {
                override fun onChanged(t: LiveDataResponse<MoviesResponse>?) {
                    remote.removeObserver(this)
                    response = t

                    if (t?.response != null) { // data arrive, insert into room and will automatically update
                        if (t.response.page == 1) {
                            local.setAll(t.response.movies)
                        } else {
                            local.addAll(t.response.movies)
                        }
                    } else { // error happened, propagate network response to UI
                        mediator.value = t
                    }
                }

            })

        mediator.addSource(
            local.getPopularMovies(page).map {
                response?.copy(response = response?.response?.copy(movies = it.response?.movies ?: emptyList())) ?: it
            }
        ) { mediator.value = it }

        return mediator
    }
}