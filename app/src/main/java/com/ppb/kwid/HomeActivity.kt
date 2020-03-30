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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button

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
