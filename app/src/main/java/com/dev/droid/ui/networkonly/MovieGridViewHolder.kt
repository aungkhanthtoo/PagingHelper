package com.dev.droid.ui.networkonly

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dev.droid.GlideApp
import com.dev.droid.data.network.IMAGE_SIZE_154
import com.dev.droid.data.network.Movie
import kotlinx.android.synthetic.main.item_grid_movie.view.*
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_movie.view.image
import kotlinx.android.synthetic.main.item_movie.view.tv_rating

class MovieGridViewHolder (itemView:View): RecyclerView.ViewHolder(itemView){
    private val image = itemView.image
    private val tvRating = itemView.tv_rating

    fun bind(data: Movie) {
        tvRating.text = data.voteAverage.toString()
        GlideApp.with(itemView.context)
            .load(IMAGE_SIZE_154 + data.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }
}