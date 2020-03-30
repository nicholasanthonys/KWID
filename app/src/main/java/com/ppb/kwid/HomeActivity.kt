package com.ppb.kwid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_home.*
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
//    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button

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

        signOut = findViewById<View>(R.id.sign_out_button) as Button
        signOut.setOnClickListener { signOut() }



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
        popularMoviesAdapter = MoviesAdapter(mutableListOf()) {movie ->showMovieDetails(movie)}
        popularMovies.adapter = popularMoviesAdapter


        topRatedMovies.layoutManager = topRatedLayoutMgr
        topRatedMoviesAdapter = MoviesAdapter(mutableListOf()) {movie ->showMovieDetails(movie)}
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

                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage ++
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

    private fun showMovieDetails(movie: Movie){
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_id,movie.id)
        Log.d("MOVIE ID", movie.id.toString())
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        startActivity(intent)
    }



    private fun setupUI() {
        sign_out_button.setOnClickListener {

        }
    }

    private fun signOut() {


        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
//        signOut = findViewById<View>(R.id.signOutBtn) as Button

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        mGoogleSignInClient.signOut().addOnCompleteListener { task: Task<Void> ->
            if (task.isSuccessful) {
                println("SIGN OUT SUKSESS")
                signOut.visibility = View.INVISIBLE
                signOut.isClickable = false

            }
            else{
                println("SIGN OUT GAGAL")
            }


        }

        startActivity(LoginActivity.getLaunchIntent(this))
    }
}
