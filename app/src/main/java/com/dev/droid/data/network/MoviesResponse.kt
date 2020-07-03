package com.dev.droid.data.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


/**
 * Created with love by A.K.HTOO on 28/06/2020,June,2020.
 */

data class MoviesResponse(
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("page")
    val page: Int = 1,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0
)

@Entity(tableName = "movies")
data class Movie(
    @SerializedName("id")
    val movieId: Long,

    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,

    @SerializedName("original_language")
    @ColumnInfo(name = "original_language") val originalLanguage: String,

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title") val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path") val posterPath: String?,

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date") val releaseDate: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average") val voteAverage: Double,

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count") val voteCount: Int
) {
    @PrimaryKey(autoGenerate = true) var _id: Long = 0
}