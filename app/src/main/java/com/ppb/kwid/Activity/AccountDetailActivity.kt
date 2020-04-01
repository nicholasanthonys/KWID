package com.ppb.kwid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ppb.kwid.R

class AccountDetailActivity : AppCompatActivity() {

    private  lateinit var btnFavoriteMovies : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        initUI()
    }

    private fun initUI(){
        btnFavoriteMovies = findViewById(R.id.btn_favorite_movies)
        btnFavoriteMovies.setOnClickListener {
            val intent = Intent(this,FavoriteMoviesActivity::class.java)
            startActivity(intent)
        }
    }
}
