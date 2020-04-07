package com.ppb.kwid.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.ppb.kwid.R

const val MESSAGE = "extra_message"

class LoginActivity : AppCompatActivity() {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 1

    //firebase instance
    private lateinit var mAuth: FirebaseAuth

    private lateinit var etLoginEmail: TextView
    private lateinit var etLoginPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tvMessage: TextView

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        //firebase instance
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initUI() {

        tvMessage = findViewById(R.id.login_error_message)

        etLoginEmail = findViewById(R.id.et_login_email)
        etLoginPassword = findViewById(R.id.et_login_password)

        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener { firebaseLogin() }

        btnRegister = findViewById(R.id.btn_register)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
        signIn.setOnClickListener { googlesignIn() }
    }

    private fun validateForm(email: String, password: String): Boolean {
        if (email.trim().isNotBlank() && password.isNotBlank()) {
            return true
        }
        updateUI(null, "Input is Invalid")
        return false
    }

    private fun firebaseLogin() {

        val email = etLoginEmail.text.trim().toString()
        val password = etLoginPassword.text.trim().toString()

        if (validateForm(email, password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                        Log.d("sign in success", "signInWithEmail:success")
                        val user = mAuth.currentUser
                        updateUI(user, "")

                    } else { // If sign in fails, display a message to the user.
                        Log.w("sign in fail", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                        updateUI(null, "sign in failed")
                    }

                }
        }
    }

    private fun googlesignIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("sign in failed", "Google sign in failed", e)
                updateUI(null, "sign in failed")
            }
        }
    }

    private fun updateUI(
        firebaseAccount: FirebaseUser?,
        error: String
    ) {
        tvMessage.text = error
        if(!error.isEmpty()){
            tvMessage.setPadding(16,10,16,10)
        }
        
        if (firebaseAccount != null) {
            startActivity(HomeActivity.getLaunchIntent(this))
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (intent.getStringExtra(MESSAGE) != "Register Sukses") {
            val currentUser = mAuth.currentUser
            updateUI(currentUser, "")
        }
        else{
           tvMessage.text = intent.getStringExtra(MESSAGE)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("sukses", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user, "")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("failed", "signInWithCredential:failure", task.exception)
                    updateUI(null, "sign in failed")
                }
            }
    }

}
