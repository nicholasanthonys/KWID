package com.ppb.kwid.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.R
import de.hdodenhof.circleimageview.CircleImageView


class AccountDetailActivity : AppCompatActivity() {

    private lateinit var btnFavoriteMovies: Button
    private lateinit var profilePicture: CircleImageView
    private lateinit var profileName: TextView

    //firebase instance
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    //get user
    val user = mAuth.currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_detail)
        initUI()

    }

    private fun initUI() {
        btnFavoriteMovies = findViewById(R.id.btn_favorite_movies)
        btnFavoriteMovies.setOnClickListener {
            val intent = Intent(this, FavoriteMoviesActivity::class.java)
            startActivity(intent)
        }

        profilePicture = findViewById(R.id.img_profile_picture)
        val imageURI = user?.photoUrl.toString()

        Glide.with(this)
            .load(imageURI)
            .placeholder(R.drawable.profile_picture)
            .into(profilePicture)

        profileName = findViewById(R.id.profile_name)
        if (user?.displayName != null) {
            profileName.text = user.displayName
        } else {
            profileName.text = user?.email
        }


    }
}
