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
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ppb.kwid.Model.Movie.Movie
import com.ppb.kwid.Model.Movie.MoviesAdapter
import com.ppb.kwid.Model.Movie.MoviesRepository
import com.ppb.kwid.Model.MovieDetail.CurrentlyShowingAdapter
import com.ppb.kwid.Model.MovieDetail.GetMovieDetailsResponse
import com.ppb.kwid.Model.MovieDetail.MovieDetailsRepository
import com.ppb.kwid.Model.Video.VideosAdapter
import com.ppb.kwid.Model.Video.VideosRepository
import com.ppb.kwid.Model.Video.VideosResponse
import com.ppb.kwid.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val CITY = "extra_city"

class HomeActivity : AppCompatActivity() {

    private var city = ""
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    //firebase auth instance
    private lateinit var mAuth: FirebaseAuth

    private lateinit var signOut: Button
    private lateinit var btnProfile: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnCity: Button
    private lateinit var btnNotification: Button

    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMoviesAdapter: MoviesAdapter
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MoviesAdapter
    private lateinit var topRatedLayoutMgr: LinearLayoutManager

    private lateinit var currentlyShowing: RecyclerView
    private lateinit var currentlyShowingAdapter: CurrentlyShowingAdapter
    private lateinit var currentlyShowingLayoutMgr: LinearLayoutManager

    private lateinit var rvVideos: RecyclerView
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var videosLayoutMgr: LinearLayoutManager

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

        btnCity = findViewById(R.id.btn_movieshow_city)
        btnCity.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

        println("is intent extra city null")
        println(intent.getStringExtra(CITY) == null)
        println(intent.getStringExtra(CITY))

        if (intent.getStringExtra(CITY) == null) {
            city = "Bandung"
        } else {
            city = intent.getStringExtra(CITY)
        }
        //set button text
        btnCity.text = city


        btnProfile = findViewById(R.id.btn_profile)
        btnProfile.setOnClickListener {
            val intent = Intent(this, AccountDetailActivity::class.java)
            startActivity(intent)
        }

        btnNotification = findViewById(R.id.btn_notification)
        btnNotification.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
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


        rvVideos = findViewById(R.id.rv_videos)
        videosLayoutMgr = LinearLayoutManager(
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



        rvVideos.layoutManager = videosLayoutMgr
        videosAdapter = VideosAdapter(mutableListOf(), mutableListOf(), this)
        rvVideos.adapter = videosAdapter



        btnRefresh = findViewById(R.id.btn_refresh)

        btnRefresh.setOnClickListener {
            refresh()
        }

        getAllResources()

    }

    private fun getAllResources() = runBlocking {
        launch(Dispatchers.Default) {
            println("Load currently showing city with thread ${Thread.currentThread().name}")
            getCurrentlyShowingCity(city)
        }

//        launch(Dispatchers.Default){
//            println("Load currently showing with thread ${Thread.currentThread().name}")
//            //getCurrentlyShowing())
//        }

        launch(Dispatchers.Default) {
            println("Load popular movie with thread ${Thread.currentThread().name}")
            getPopularMovies()
        }

        launch(Dispatchers.Default) {
            println("Load Top Rated with thread ${Thread.currentThread().name}")
            getTopRatedMovies()
        }


        launch(Dispatchers.Default) {
            println("Load getMovieVideos with thread ${Thread.currentThread().name}")
            getMovieVideos()
        }



    }


    private fun refresh() {


        //refresh currently showing
        currentlyShowing.layoutManager = currentlyShowingLayoutMgr
        currentlyShowingAdapter =
            CurrentlyShowingAdapter(mutableListOf()) { movie ->
                showMovieDetails(movie)
            }
        currentlyShowing.adapter = currentlyShowingAdapter

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

        rvVideos.layoutManager = videosLayoutMgr
        videosAdapter = VideosAdapter(mutableListOf(), mutableListOf(), this)
        rvVideos.adapter = videosAdapter


        //getCurrentlyShowing()
        getCurrentlyShowingCity(city)
        getPopularMovies()
        getTopRatedMovies()
        getMovieVideos()


    }

    private fun getMovieVideos() {

        db.collection("videosShowCase").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var id = document.data.get("id").toString().toLong()
                    VideosRepository.getMovieVideos(
                        id,
                        ::onVideosFetched,
                        ::onError
                    )
                }
            }
    }

    private fun onVideosFetched(videosResponse: VideosResponse) {
        if (!videosResponse.videos.videoResults.isNullOrEmpty()) {
            videosAdapter.updateVideo(videosResponse, videosResponse.videos.videoResults[0])
        }

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

    class MovieCity(
        var city: String? = null,
        var movie_id: List<String>? = null
    )

    private fun getCurrentlyShowingCity(city: String) {
        println("Menjalankan fungsi city")
        var docRef = db.collection("currentlyShowingCity").document(city)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val myMovieCity = documentSnapshot.toObject<MovieCity>()

            myMovieCity?.movie_id?.iterator()?.forEach {
                println("item is " + it)
                MovieDetailsRepository.getMovieDetails(
                    id = it.toLong(),
                    onSuccess = ::onCurrentlyShowingMoviesFetched,
                    onError = ::onError
                )
            }
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
