package com.ppb.kwid.Model.MovieDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppb.kwid.R

class CurrentlyShowingAdapter(
    private var movies: MutableList<GetMovieDetailsResponse>,
    private val onMovieClick: (movie: GetMovieDetailsResponse) -> Unit
) : RecyclerView.Adapter<CurrentlyShowingAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(movie: GetMovieDetailsResponse, isRefresh: Boolean) {
        this.movies.add(movie)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val tvMovieName: TextView = itemView.findViewById(R.id.item_movie_name)

        fun bind(movie: GetMovieDetailsResponse) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .centerCrop()
                .into(poster)
            tvMovieName.text = movie.title
            itemView.setOnClickListener { onMovieClick.invoke(movie) }
        }
    }
}