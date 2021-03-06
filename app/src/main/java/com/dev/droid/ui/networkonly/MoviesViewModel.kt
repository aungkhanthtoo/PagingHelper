package com.dev.droid.ui.networkonly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dev.droid.data.MovieRepositoryImpl
import com.droiddev.paging.PagingHelper

class MoviesViewModel : ViewModel(), PagingHelper.Callback {

    private val _pages = MutableLiveData<Int>()
    val topRatedMovies = _pages.switchMap { MovieRepositoryImpl.getPopularMovies(it) }

    override fun onLoadMore(nextPage: Int) {
        _pages.value = nextPage
    }

}