package com.dev.droid.data.network

import com.dev.droid.SampleApp
import com.dev.droid.data.network.livedatacalladapter.LiveDataCallAdapterFactory
import com.dev.droid.data.network.livedatacalladapter.ResponseLiveData
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */

const val API_KEY = "fe5a4d0f642574a3602932f7976b6de1"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w342"
const val IMAGE_SIZE_185 = "https://image.tmdb.org/t/p/w185"
const val IMAGE_SIZE_154 = "https://image.tmdb.org/t/p/w154"

const val PARAM_API_KEY = "api_key"
const val PARAM_PAGE = "page"

const val TOP_RATED = "movie/top_rated"
const val POPULAR = "movie/popular"
const val UPCOMING = "movie/upcoming"

interface MovieService {

    @GET(POPULAR)
    fun popularMovies(
        @Query(PARAM_PAGE) page: Int,
        @Query(PARAM_API_KEY) key: String = API_KEY
    ): ResponseLiveData<MoviesResponse>

    companion object {

        fun create(): MovieService {
            val okHttpBuilder = OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(SampleApp.appContext))

            return Retrofit.Builder().baseUrl(BASE_URL)
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .build()
                .create(MovieService::class.java)
        }
    }
}