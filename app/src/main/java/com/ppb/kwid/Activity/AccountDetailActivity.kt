package com.ppb.kwid.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.Database.DatabaseHelper
import com.ppb.kwid.R
import de.hdodenhof.circleimageview.CircleImageView


class AccountDetailActivity : AppCompatActivity() {

    private lateinit var btnFavoriteMovies: Button
    private lateinit var btnTopUp: Button
    private lateinit var btnTransactionHistory: Button
    private lateinit var profilePicture: CircleImageView
    private lateinit var profileName: TextView
    private lateinit var etUsername: EditText
    //    private lateinit var btnEditUsername: Button
    private lateinit var btnBack: ImageView

    //firebase instance
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //instance db helper
    private var dbHelper = DatabaseHelper(this)

    //get user
    val user = mAuth.currentUser
    val email = user?.email.toString()


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

        btnBack = findViewById(R.id.button_back_account_detail)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        //get username frm local database
        var username = dbHelper.getUsername(user?.email)
        if (username.isNullOrBlank()) {
            //ambil dari firebase
            username = user?.displayName.toString()
        }
        profileName = findViewById(R.id.profile_name)
        //set profile name as username
        profileName.text = username

        etUsername = findViewById(R.id.edit_username)

//        btnEditUsername = findViewById(R.id.btn_edit_username)
        profileName.setOnClickListener {
            //if button edit is clicked, profilename, button edit will gone
            //edit text will appear
            profileName.visibility = View.GONE
            etUsername.visibility = View.VISIBLE
//            btnEditUsername.visibility = View.GONE

            //set cursor focus to edit text
            etUsername.setText(dbHelper.getUsername(user?.email))
            var position = etUsername.length()
            var etext: Editable = etUsername.text
            Selection.setSelection(etext, position)
            etUsername.requestFocus()

            //after user press enter, input keyboard will be hidden
            etUsername.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) { // do something, e.g. set your TextView here via .setText()
                    val username = etUsername.text.toString()
                    if (username.isNotEmpty()) {
                        dbHelper.updateUsername(username, email)
                        profileName.text = username
                        profileName.visibility = View.VISIBLE
//                        btnEditUsername.visibility = View.VISIBLE
                        etUsername.visibility = View.GONE

                        Toast.makeText(this, "username updated", Toast.LENGTH_SHORT).show()
                        val imm: InputMethodManager =
                            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    } else {
                        Toast.makeText(this, "username cannot be empty", Toast.LENGTH_SHORT).show()
                    }

                    return@OnEditorActionListener true
                }
                false
            })

        }

        profilePicture = findViewById(R.id.img_profile_picture)
        val imageURI = user?.photoUrl.toString()

        Glide.with(this)
            .load(imageURI)
            .placeholder(R.drawable.profile_picture)
            .into(profilePicture)

        btnTopUp = findViewById(R.id.btn_top_up)
        btnTopUp.setOnClickListener {
            val intent = Intent(this, TopUpActivity::class.java)
            startActivity(intent)
        }

        btnTransactionHistory = findViewById(R.id.btn_transaction_history)
        btnTransactionHistory.setOnClickListener {
            val intent = Intent(this, TransactionHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
