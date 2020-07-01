package com.dev.droid.data

import com.dev.droid.data.network.MovieService
import com.dev.droid.data.network.MoviesResponse
import com.dev.droid.data.network.livedatacalladapter.ResponseLiveData

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */
object MovieRemoteDataSource : MovieRepository {

    private val service: MovieService = MovieService.create()

    override fun getPopularMovies(page: Int): ResponseLiveData<MoviesResponse> {
        return service.popularMovies(page)
    }

}