package com.dev.droid.ui.networkroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.dev.droid.R
import com.dev.droid.data.network.Movie
import com.dev.droid.ui.networkonly.MovieViewHolder
import com.droiddev.paging.PagingAdapter

/**
 * Created with love by A.K.HTOO on 01/07/2020,July,2020.
 */
class MoviePagingAdapter : PagingAdapter<Movie, MovieViewHolder>(DIFF_CALLBACK) {

    override fun getLoadingItemLayoutRes(): Int {
        return R.layout.item_loading
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindItemViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.movieId == newItem.movieId
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }
    }
}