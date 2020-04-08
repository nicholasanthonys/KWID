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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ppb.kwid.Model.Movie.*
import com.ppb.kwid.Model.MovieDetail.CurrentlyShowingAdapter
import com.ppb.kwid.Model.MovieDetail.GetMovieDetailsResponse
import com.ppb.kwid.Model.MovieDetail.MovieDetailsRepository
import com.ppb.kwid.R

class HomeActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    //firebase auth instance
    private lateinit var mAuth: FirebaseAuth

    private lateinit var signOut: Button
    private lateinit var btnProfile: Button
    private lateinit var btnRefresh: Button

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedLayoutMgr: LinearLayoutManager

    private lateinit var currentlyShowing: RecyclerView
    private lateinit var currentlyShowingAdapter: CurrentlyShowingAdapter
    private lateinit var currentlyShowingLayoutMgr: LinearLayoutManager

    private var popularMoviesPage = 1
    private var topRatedMoviesPage = 1

    // Access a Cloud Firestore instance from your Activity
    private val db = Firebase.firestore

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initUI()

        //firebase instance
        mAuth = FirebaseAuth.getInstance()
        println("nama user : " + mAuth.currentUser?.email.toString())
        println("id user : " + mAuth.currentUser?.uid)

    }

    private fun initUI() {
        signOut = findViewById<View>(R.id.sign_out_button) as Button
        signOut.setOnClickListener { signOut() }

        btnProfile = findViewById(R.id.btn_profile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, AccountDetailActivity::class.java)
            startActivity(intent)
        }

        popularMovies = findViewById(R.id.popular_movies)
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

        currentlyShowing = findViewById(R.id.currently_showing_movies)
        currentlyShowingLayoutMgr = LinearLayoutManager(
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

        currentlyShowing.layoutManager = currentlyShowingLayoutMgr
        currentlyShowingAdapter =
            CurrentlyShowingAdapter(mutableListOf()) { movie ->
                showMovieDetails(movie)
            }
        currentlyShowing.adapter = currentlyShowingAdapter

        btnRefresh = findViewById(R.id.btn_refresh)

        btnRefresh.setOnClickListener {
            refresh()
        }

        getCurrentlyShowing()
        getPopularMovies()
        getTopRatedMovies()
    }

    private fun refresh() {
        //refresh currently showing
//        currentlyShowing.layoutManager = currentlyShowingLayoutMgr
//        currentlyShowingAdapter =
//            CurrentlyShowingAdapter(mutableListOf()) { movie ->
//                showMovieDetails(movie)
//            }
//        currentlyShowing.adapter = currentlyShowingAdapter
        getCurrentlyShowing()
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


    private fun showMovieDetails(movie: GetMovieDetailsResponse) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_id, movie.id)
        Log.d("MOVIE ID", movie.id.toString())
        startActivity(intent)
    }

    //currently showing
    private fun getCurrentlyShowing() {
        db.collection("currentlyShowing")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("DB Read Sukses", "${document.id} => ${document.data.get("id")}")

                    MovieDetailsRepository.getMovieDetails(
                        id = document.data.get("id").toString().toLong(),
                        onSuccess = ::onCurrentlyShowingMoviesFetched,
                        onError = ::onError
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.d("DB Read Gagal", "Error getting documents: ", exception)
            }
    }


    private fun onCurrentlyShowingMoviesFetched(movie: GetMovieDetailsResponse) {
        currentlyShowingAdapter.updateMovies(movie, false)
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
