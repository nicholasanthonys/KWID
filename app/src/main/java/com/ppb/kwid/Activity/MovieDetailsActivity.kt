package com.ppb.kwid.Activity

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.Database.DatabaseHelper
import com.ppb.kwid.Fragment.CastCrewFragment
import com.ppb.kwid.Fragment.OverviewFragment
import com.ppb.kwid.Fragment.ScheduleFragment
import com.ppb.kwid.Model.Credits.Cast
import com.ppb.kwid.Model.Credits.CreditsRepository
import com.ppb.kwid.Model.Credits.Crew
import com.ppb.kwid.Model.Genre.Genres
import com.ppb.kwid.Model.MovieDetail.GetMovieDetailsResponse
import com.ppb.kwid.Model.MovieDetail.MovieDetailsRepository
import com.ppb.kwid.Model.MovieDetail.MovieDetailsSectionsPageAdapter
import com.ppb.kwid.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_movie_details.*


const val MOVIE_id = "extra_movie_id"


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var duration: TextView
    private lateinit var director: TextView
    private lateinit var btnLiked: Button
//    private lateinit var btnOverview: Button
//    private lateinit var btnCastCrew: Button
//    private lateinit var btnSchedule: Button


    //instance db helper
    private var dbHelper = DatabaseHelper(this)

    // Access a Cloud Firestore instance from your Activity to get USER UUID
    private val mAuth = FirebaseAuth.getInstance()

    private val userId = mAuth.currentUser!!.uid
    private var movieId: Long = 0
    private var movieName = ""
    private var movieGenre = ""
    private var movieDirector = ""
    private var overview = "ini overview"
    private var isLiked: Boolean = false

    //inisialisasi
    private var fragmentOverview = OverviewFragment.newInstance(overview)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        initUI()
        println(" this movie id adalah  " + this.overview)

    }

    private fun initUI() {
        backdrop = findViewById(R.id.movie_backdrop)
        poster = findViewById(R.id.movie_poster)
        title = findViewById(R.id.movie_title)
        rating = findViewById(R.id.rating_mov_detail)
        releaseDate = findViewById(R.id.movie_release_date)
        //overview = findViewById(R.id.movie_overview) //di fragment overview
        duration = findViewById(R.id.movie_duration)
        director = findViewById(R.id.movie_director)

        movieId = intent.getLongExtra(MOVIE_id, 0)


        btnLiked = findViewById(R.id.btn_liked)

        /* MENENTUKAN LIKE */
        isLiked = dbHelper.isCurrentMovieLiked(userId, movieId.toString())
        if (isLiked) {
            btnLiked.setBackgroundResource(R.drawable.ant_designheart_filled)
        }
        btnLiked.setOnClickListener {
            handleClick()

        }

        CreditsRepository.getCasts(
            id = movieId,
            onSuccess = ::onCreditsFetched,
            onError = ::onError
        )

        MovieDetailsRepository.getMovieDetails(
            id = intent.getLongExtra(MOVIE_id, 2),
            onSuccess = ::onMovieDetailsFetched,
            onError = ::onError
        )
    }

//    private fun setUpButtonFragment(overview: String) {
//        //Button to change fragment
//        btnOverview = findViewById(R.id.btn_overview)
//        btnCastCrew = findViewById(R.id.btn_cast_and_crew)
//        btnSchedule = findViewById(R.id.btn_schedule)
//
//        btnOverview.setOnClickListener {
//            changeFragment(1, overview)
//
//            //Setting the button
//            btnOverview.isEnabled = false
//            btnOverview.isClickable = false
//            btnCastCrew.isEnabled = true
//            btnCastCrew.isClickable = true
//            btnSchedule.isEnabled = true
//            btnSchedule.isClickable = true
//        }
//
//        btnCastCrew.setOnClickListener {
//            changeFragment(2, "")
//
//            //Setting the button
//            btnOverview.isEnabled = true
//            btnOverview.isClickable = true
//            btnCastCrew.isEnabled = false
//            btnCastCrew.isClickable = false
//            btnSchedule.isEnabled = true
//            btnSchedule.isClickable = true
//        }
//
//        btnSchedule.setOnClickListener {
//            changeFragment(3, "")
//
//            //Setting the button
//            btnOverview.isEnabled = true
//            btnOverview.isClickable = true
//            btnCastCrew.isEnabled = true
//            btnCastCrew.isClickable = true
//            btnSchedule.isEnabled = false
//            btnSchedule.isClickable = false
//        }
//
//        //Initialize fragment with overview
//        btnOverview.isEnabled = false
//        btnOverview.isClickable = false
//
//
//    }


    private fun handleClick() {
        if (isLiked) {
            //Then Dislike the movie
            //delete favorite
            dbHelper.deleteFavoriteMovies(userId, movieId.toString())
            isLiked = false
            btnLiked.setBackgroundResource(R.drawable.ant_designheart_outlined)
            showToast("You dislike $movieName ")

        } else {
            //Then Like the movie
            //insert favorite
            btnLiked.setBackgroundResource(R.drawable.ant_designheart_filled)
            isLiked = true
            dbHelper.insertTableFavoriteMovies(
                userId,
                movieId.toString(),
                movieName,
                movieGenre,
                movieDirector
            )
            showToast("You like $movieName")
        }

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
        director.text = movieDirector
    }

    private fun onMovieDetailsFetched(movDetails: GetMovieDetailsResponse) {
        val extras = intent.extras
        if (extras != null) {

            val multi = MultiTransformation<Bitmap>(
                RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.BOTTOM)
            )

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280${movDetails.backdropPath}")
                .apply((RequestOptions.bitmapTransform(multi)))
                .into(backdrop)

            backdrop.clipToOutline = true

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342${movDetails.posterPath}")
                .transform(CenterCrop())
                .into(poster)

            movieName = movDetails.title

            title.text = movieName
            rating.rating = movDetails.rating / 2
            releaseDate.text = movDetails.releaseDate

            overview = movDetails.overview

            duration.text = movDetails.duration.toString() + " minutes"

            val listGenres: List<Genres> = movDetails.genres
            for (i in listGenres.indices) {
                if (i > 0) {
                    this.movieGenre += ", "
                }
                this.movieGenre += listGenres[i].name
            }

            //set up button fragment
//            setUpButtonFragment(overview)

            //set up overview fragment
//            fragmentOverview = OverviewFragment.newInstance(overview)
//            supportFragmentManager.beginTransaction()
//                .add(R.id.myfragmentmovie_detail, fragmentOverview)
//                .commit()

            //Tablayout
            val sectionsPagerAdapter = MovieDetailsSectionsPageAdapter(this, supportFragmentManager)
            sectionsPagerAdapter.setAllParameters(overview, movieId)
            view_pager.adapter = sectionsPagerAdapter
            tabs_movie_detail.setupWithViewPager(view_pager)
            supportActionBar?.elevation = 0f
        } else {
            finish()
        }
    }

//    private fun changeFragment(id: Int, overview: String) {
//        val transaction = supportFragmentManager.beginTransaction()
//        var fragment: Fragment? = null
//
//        if (id == 1) {
//            fragment = OverviewFragment.newInstance(overview)
//            transaction
//                .replace(R.id.myfragmentmovie_detail, fragment)
//                .commit()
//        } else if (id == 2) {
//            fragment = CastCrewFragment.newInstance(movieId)
//            transaction
//                .replace(R.id.myfragmentmovie_detail, fragment)
//                .commit()
//        } else if (id == 3) {
//            fragment = ScheduleFragment()
//            transaction
//                .replace(R.id.myfragmentmovie_detail, fragment)
//                .commit()
//        }
//    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun showToast(text: String) {
        Toast.makeText(this@MovieDetailsActivity, text, Toast.LENGTH_LONG).show()
    }

}




