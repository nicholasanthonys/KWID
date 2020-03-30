package com.ppb.kwid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {


    lateinit var gso: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signOut = findViewById<View>(R.id.signOutBtn) as Button

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn.setOnClickListener {signIn() }

    }

    private fun signIn() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        } else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }

    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            updateUI(account)

        } catch (e: ApiException) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()

        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {

        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
        dispTxt.text = account!!.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                    task: Task<Void> ->
                if (task.isSuccessful) {
                    dispTxt.text = " "
                    signOut.visibility = View.INVISIBLE
                    signOut.isClickable = false
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            updateUI(account)
        }else{
            println("LU UDAH SIGNOUT KUDU SIGN IN LAGI")
        }
    }



}
