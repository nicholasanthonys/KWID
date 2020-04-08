package com.ppb.kwid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.Database.DatabaseHelper
import com.ppb.kwid.Model.Movie.Movie
import com.ppb.kwid.Model.MovieDetail.GetMovieDetailsResponse
import com.ppb.kwid.Model.MovieDetail.MovieDetailsRepository
import com.ppb.kwid.Model.MovieDetail.MovieFavoriteAdapter
import com.ppb.kwid.R

class FavoriteMoviesActivity : AppCompatActivity() {
    //instance db helper
    private var dbHelper = DatabaseHelper(this)
    private val mAuth = FirebaseAuth.getInstance()
    var listIDMovie = mutableListOf<String>()

    private lateinit var movieFavoriteRecycler: RecyclerView
    private lateinit var movieFavoriteAdapter: MovieFavoriteAdapter
    private lateinit var movieFavoriteLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movies)



        movieFavoriteRecycler = findViewById(R.id.rv_favorite_movies)
        movieFavoriteLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        movieFavoriteRecycler.layoutManager = movieFavoriteLayoutManager
        movieFavoriteAdapter =
            MovieFavoriteAdapter(mutableListOf()) { movie ->
                showMovieDetails(movie)
            }
        movieFavoriteRecycler.adapter = movieFavoriteAdapter


        getFavoriteMovie()
    }

    private fun getFavoriteMovie() {
        listIDMovie = dbHelper.getAllFavoriteMovies(mAuth.currentUser!!.uid)
        println("LIST ID MOVIE UKURAN" + listIDMovie.size)
        for (item in listIDMovie) {
            MovieDetailsRepository.getMovieDetails(
                id = item.toLong(),
                onSuccess = ::onFavoriteMoviesFetched,
                onError = ::onError
            )
        }
    }

    private fun onFavoriteMoviesFetched(movie: GetMovieDetailsResponse) {
        movieFavoriteAdapter.updateMovies(movie, false)
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetails(movie: GetMovieDetailsResponse) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_id, movie.id)
        Log.d("MOVIE ID", movie.id.toString())
        startActivity(intent)
    }


}
