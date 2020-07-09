package com.dev.droid.ui.networkroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.dev.droid.R
import com.dev.droid.data.network.Movie
import com.dev.droid.ui.networkonly.MovieGridViewHolder
import com.dev.droid.ui.networkonly.MovieViewHolder
import com.droiddev.paging.PagingAdapter

class MovieGridPagingAdapter :PagingAdapter<Movie,MovieGridViewHolder>(DIFF_CALLBACK){
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

    override fun getLoadingItemLayoutRes(): Int {
        return R.layout.item_loading
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): MovieGridViewHolder {
        return MovieGridViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_movie, parent, false)
        )
    }

    override fun onBindItemViewHolder(holder: MovieGridViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}