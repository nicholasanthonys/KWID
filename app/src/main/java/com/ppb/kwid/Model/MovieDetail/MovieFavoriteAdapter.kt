package com.ppb.kwid.Model.MovieDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppb.kwid.Model.Credits.Cast
import com.ppb.kwid.Model.Credits.CreditsRepository
import com.ppb.kwid.Model.Credits.Crew
import com.ppb.kwid.Model.Genre.Genres
import com.ppb.kwid.R

class MovieFavoriteAdapter(
    private var movies: MutableList<GetMovieDetailsResponse>,
    private val onMovieClick: (movie: GetMovieDetailsResponse) -> Unit
) : RecyclerView.Adapter<MovieFavoriteAdapter.MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_favorite_movies, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(movie: GetMovieDetailsResponse, isRefresh: Boolean) {
        this.movies.add(movie)
        notifyItemInserted(this.movies.size)
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.img_favorite_movie_poster)
        private val tvMovieName: TextView = itemView.findViewById(R.id.txt_favorite_movie_name)
        private val tvGenre: TextView = itemView.findViewById(R.id.txt_genre)
        private var tvDirector: TextView = itemView.findViewById(R.id.txt_director)
        private var tvReleaseDate: TextView = itemView.findViewById(R.id.txt_release_date)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.fav_movie_rating)
        var movieGenre = ""
        var movieDirector = ""

        fun bind(movie: GetMovieDetailsResponse) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .centerCrop()
                .into(poster)

            val listGenres: List<Genres> = movie.genres
            for (i in listGenres.indices) {
                if (i > 0) {
                    movieGenre += ", "
                }
                movieGenre += listGenres[i].name
            }

            tvGenre.text = movieGenre
            tvMovieName.text = movie.title
            tvReleaseDate.text = movie.releaseDate
            ratingBar.rating = (movie.rating / 2)
            itemView.setOnClickListener { onMovieClick.invoke(movie) }


            CreditsRepository.getCasts(
                id = movie.id,
                onSuccess = ::onCreditsFetched,
                onError = ::onError
            )
        }

        private fun onCreditsFetched(cast: List<Cast>, crews: List<Crew>) {
            //update textview crew
            for (i in crews.indices) {
                if (crews[i].job == "Director") {
                    if (movieDirector.isNotEmpty()) {
                        movieDirector += ", "
                    }
                    movieDirector += crews[i].name
                }

            }
            //set textview
            tvDirector.text = movieDirector
        }

        private fun onError() {
            println("ERROR FROM MOVIE FAVORITE ADAPTER")
        }

    }
}