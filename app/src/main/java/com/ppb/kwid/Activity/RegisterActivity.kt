package com.ppb.kwid.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ppb.kwid.Database.DatabaseHelper
import com.ppb.kwid.R


class RegisterActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var btnRegister: Button
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var tvError: TextView
    lateinit var etUsername: TextView
    lateinit var etConfirmPasssword: TextView

    //instance db helper
    private var dbHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        initUI()
    }

    private fun initUI() {
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        etConfirmPasssword = findViewById(R.id.et_confirm_password)
        tvError = findViewById(R.id.error_message)
        etUsername = findViewById(R.id.et_username)

        tvError.text = ""
        tvError.visibility = View.INVISIBLE

        btnRegister = findViewById(R.id.btn_register_submit)
        btnRegister.setOnClickListener {
            registerSubmit()
        }
    }

    private fun validateForm(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            updateUI(null, "Please complete all the form")
        } else {
            if (etPassword.text.length >= 6) {
                if (password == confirmPassword) {
                    return true
                } else {
                    updateUI(null, "Password doesn't match")
                }
            } else {
                updateUI(null, "password must be 6 character or more")
            }
        }

        return false
    }

    private fun updateUI(user: FirebaseUser?, error: String) {
        if (user != null) {

            user.sendEmailVerification()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("email sent?", "Email sent.")
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra(MESSAGE, resources.getString(R.string.register_success))
                        startActivity(intent)
                    } else {
                        Log.d("email sent?", "Email failed")
                    }
                }


        } else {
            tvError.visibility = View.VISIBLE
            tvError.text = error
            if (error.isNotEmpty()) {
                tvError.setPadding(16, 10, 16, 10)
            }
        }
    }

    private fun registerSubmit() {
        val username = etUsername.text.trim().toString()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPasssword.text.toString()

        if (validateForm(username, email, password, confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    override fun onComplete(task: Task<AuthResult?>) {
                        println("email is  " + email + " password is " + password)
                        if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                            Log.d("register success", "createUserWithEmail:success")
                            val user = mAuth.currentUser


                            //disini masukkan usernamere ke database lokal

                            dbHelper.insertUser(username, email)





                            updateUI(user, "")
                        } else { // If sign in fails, display a message to the user.
                            Log.w(
                                "register failur",
                                "createUserWithEmail:failure",
                                task.exception
                            )

                            Toast.makeText(applicationContext, "Input not valid", Toast.LENGTH_LONG)
                                .show()
                            updateUI(null, "Register Failed")
                        }
                    }

                })
        }
    }
}
