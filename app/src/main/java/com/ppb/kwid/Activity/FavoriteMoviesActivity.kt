package com.ppb.kwid.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.Database.DatabaseHelper
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

    private lateinit var tvMessageEmpty: TextView

    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_movies)

        tvMessageEmpty = findViewById(R.id.message_favorite_movie_empty)

        movieFavoriteRecycler = findViewById(R.id.rv_favorite_movies)
        movieFavoriteLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        btnBack = findViewById(R.id.btn_back_favorite_movies)
        btnBack.setOnClickListener {
            onBackPressed()
        }

//        movieFavoriteRecycler.layoutManager = movieFavoriteLayoutManager
//        movieFavoriteAdapter =
//            MovieFavoriteAdapter(mutableListOf()) { movie ->
//                showMovieDetails(movie)
//            }
//        movieFavoriteRecycler.adapter = movieFavoriteAdapter
//
//
//        getFavoriteMovie()


    }


    override fun onResume() {
        super.onResume()
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

        if (listIDMovie.isNotEmpty()) {
            tvMessageEmpty.visibility = View.GONE
        } else {
            tvMessageEmpty.visibility = View.VISIBLE
        }

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
