package com.dev.droid.ui.networkonly

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.droid.R
import com.dev.droid.data.network.Movie
import com.droiddev.paging.LegacyPagingAdapter

/**
 * Created with love by A.K.HTOO on 30/06/2020,June,2020.
 */
class MovieAdapter : LegacyPagingAdapter<Movie, MovieViewHolder>() {

    override fun getLoadingItemLayoutRes(): Int  = R.layout.item_loading

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindItemViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

}