package com.dev.droid.ui.networkonly

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dev.droid.GlideApp
import com.dev.droid.data.network.IMAGE_SIZE_185
import com.dev.droid.data.network.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

/**
 * Created with love by A.K.HTOO on 30/06/2020,June,2020.
 */
class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val image = itemView.image
    private val tvTitle = itemView.tv_title
    private val tvDate = itemView.tv_date
    private val tvRating = itemView.tv_rating

    fun bind(data: Movie) {
        tvTitle.text = data.originalTitle
        tvDate.text = data.releaseDate
        tvRating.text = data.voteAverage.toString()
        GlideApp.with(itemView.context)
            .load(IMAGE_SIZE_185 + data.posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }

}