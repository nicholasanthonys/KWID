package com.ppb.kwid.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ppb.kwid.R

class ReviewActivity : AppCompatActivity() {

    companion object {
        const val TITLE = "movie_title"
        const val POSTER = "movie_poster"
    }

    private lateinit var btnStar1: ImageView
    private lateinit var btnStar2: ImageView
    private lateinit var btnStar3: ImageView
    private lateinit var btnStar4: ImageView
    private lateinit var btnStar5: ImageView
    private lateinit var moviePoster: ImageView
    private lateinit var btnBackReview: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var btnPublish: Button
    private lateinit var btnBack: Button
    private var url: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        title = intent.getStringExtra(TITLE)
        url = intent.getStringExtra(POSTER)

        settingRatingButton()

        txtTitle = findViewById(R.id.txt_movie_title)
        txtTitle.text = title

        moviePoster = findViewById(R.id.img_movie)
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(moviePoster)

        btnBack = findViewById(R.id.btn_back_review)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnPublish = findViewById(R.id.btn_publish)
        btnPublish.setOnClickListener {
            
        }

        btnBackReview = findViewById(R.id.btn_back_review)
        btnBackReview.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun settingRatingButton() {
        btnStar1 = findViewById(R.id.btn_img_star_1)
        btnStar2 = findViewById(R.id.btn_img_star_2)
        btnStar3 = findViewById(R.id.btn_img_star_3)
        btnStar4 = findViewById(R.id.btn_img_star_4)
        btnStar5 = findViewById(R.id.btn_img_star_5)

        btnStar1.setOnClickListener {
            btnStar1.setImageResource(R.drawable.icround_star)
            btnStar2.setImageResource(R.drawable.star_gray)
            btnStar3.setImageResource(R.drawable.star_gray)
            btnStar4.setImageResource(R.drawable.star_gray)
            btnStar5.setImageResource(R.drawable.star_gray)
        }

        btnStar2.setOnClickListener {
            btnStar1.setImageResource(R.drawable.icround_star)
            btnStar2.setImageResource(R.drawable.icround_star)
            btnStar3.setImageResource(R.drawable.star_gray)
            btnStar4.setImageResource(R.drawable.star_gray)
            btnStar5.setImageResource(R.drawable.star_gray)
        }

        btnStar3.setOnClickListener {
            btnStar1.setImageResource(R.drawable.icround_star)
            btnStar2.setImageResource(R.drawable.icround_star)
            btnStar3.setImageResource(R.drawable.icround_star)
            btnStar4.setImageResource(R.drawable.star_gray)
            btnStar5.setImageResource(R.drawable.star_gray)
        }

        btnStar4.setOnClickListener {
            btnStar1.setImageResource(R.drawable.icround_star)
            btnStar2.setImageResource(R.drawable.icround_star)
            btnStar3.setImageResource(R.drawable.icround_star)
            btnStar4.setImageResource(R.drawable.icround_star)
            btnStar5.setImageResource(R.drawable.star_gray)
        }

        btnStar5.setOnClickListener {
            btnStar1.setImageResource(R.drawable.icround_star)
            btnStar2.setImageResource(R.drawable.icround_star)
            btnStar3.setImageResource(R.drawable.icround_star)
            btnStar4.setImageResource(R.drawable.icround_star)
            btnStar5.setImageResource(R.drawable.icround_star)
        }
    }
}
