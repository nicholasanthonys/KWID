package com.ppb.kwid.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ppb.kwid.Model.Movie.Movie
import com.ppb.kwid.Model.Movie.MoviesAdapter
import com.ppb.kwid.Model.Movie.MoviesRepository
import com.ppb.kwid.R


class HomeActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    //firebase auth instance
    private lateinit var mAuth: FirebaseAuth

    private lateinit var signOut: Button

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedLayoutMgr: LinearLayoutManager

    private var popularMoviesPage = 1
    private var topRatedMoviesPage = 1

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()

        //firbase instance
        mAuth = FirebaseAuth.getInstance()
        println("nama user : " + mAuth.currentUser?.email.toString())

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        popularMovies.layoutManager = popularMoviesLayoutMgr
        popularMoviesAdapter =
            MoviesAdapter(mutableListOf()) { movie ->
                showMovieDetails(movie)
            }
        popularMovies.adapter = popularMoviesAdapter

        topRatedMovies.layoutManager = topRatedLayoutMgr
        topRatedMoviesAdapter =
            MoviesAdapter(mutableListOf()) { movie ->
                showMovieDetails(movie)
            }
        topRatedMovies.adapter = topRatedMoviesAdapter

        getPopularMovies()
        getTopRatedMovies()
    }

    private fun onPopularMoviesFetched(movies: MutableList<Movie>) {
        popularMoviesAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun getPopularMovies() {
        MoviesRepository.getPopularMovies(
            popularMoviesPage,
            onSuccess = ::onPopularMoviesFetched,
            onError = ::onError
        )
    }

    private fun attachPopularMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
//                    Log.d("MainActivity", "Fetching movies")
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedLayoutMgr.itemCount
                val visibleItemCount = topRatedLayoutMgr.childCount
                val firstVisibleItem = topRatedLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }

    private fun getTopRatedMovies() {
        MoviesRepository.getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    private fun onTopRatedMoviesFetched(movies: MutableList<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_id, movie.id)
        Log.d("MOVIE ID", movie.id.toString())
        startActivity(intent)
    }

    private fun setupUI() {
        signOut = findViewById<View>(R.id.sign_out_button) as Button
        var accountGoogle = GoogleSignIn.getLastSignedInAccount(this)
        println("account google is null ?" + accountGoogle)
        signOut.setOnClickListener { signOut() }
    }

    private fun signOut() {
        mAuth.signOut()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut().addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                println("SIGN OUT SUKSESS")
                signOut.visibility = View.INVISIBLE
                signOut.isClickable = false

            } else {
                println("SIGN OUT GAGAL")
            }
        }
        startActivity(LoginActivity.getLaunchIntent(this))
    }
}
