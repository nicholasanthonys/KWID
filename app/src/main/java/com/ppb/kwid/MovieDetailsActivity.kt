package com.ppb.kwid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

const val MOVIE_id = "extra_movie_id"


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var rv_casts: RecyclerView
    private lateinit var creditAdapter: CreditsAdapter
//    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager

    private lateinit var backdrop: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var releaseDate: TextView
    private lateinit var overview: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        backdrop = findViewById(R.id.movie_backdrop)
        poster = findViewById(R.id.movie_poster)
        title = findViewById(R.id.movie_title)
        rating = findViewById(R.id.movie_rating)
        releaseDate = findViewById(R.id.movie_release_date)
        overview = findViewById(R.id.movie_overview)

        rv_casts = findViewById(R.id.cast_and_crew)
        rv_casts.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        creditAdapter = CreditsAdapter(listOf(), listOf())
        rv_casts.adapter = creditAdapter
        

        CreditsRepository.getCasts(
            id = intent.getLongExtra(MOVIE_id, 2),
            onSuccess = ::onCreditsFetched,
            onError = ::onError
        )


        MovieDetailsRepository.getMovieDetails(
            id = intent.getLongExtra(MOVIE_id, 2),
            onSuccess = ::onMovieDetailsFetched,
            onError = ::onError
        )
    }


    private fun onCreditsFetched(cast: List<Cast>, crews: List<Crew>) {
        creditAdapter.updateCasts(cast,crews)
        //update textview crew
        println("Crew : " + crews)
    }

    private fun onMovieDetailsFetched(movDetails: GetMovieDetailsResponse) {
        val extras = intent.extras
        if (extras != null) {

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280${movDetails.backdropPath}")
                .transform(CenterCrop())
                .into(backdrop)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342${movDetails.posterPath}")
                .transform(CenterCrop())
                .into(poster)

            title.text = movDetails.title
            rating.rating = movDetails.rating
            releaseDate.text = movDetails.releaseDate
            overview.text = movDetails.overview

            val listGenres: List<Genres> = movDetails.genres
            var textGenre = ""
            for (i in listGenres.indices) {
                if (i > 0) {
                    textGenre += ", "
                }
                textGenre += listGenres[i].name
            }

        } else {
            finish()
        }

    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

}

