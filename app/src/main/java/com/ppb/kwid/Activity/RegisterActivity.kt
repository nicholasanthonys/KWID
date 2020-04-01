package com.ppb.kwid.Activity

import android.R.attr
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
import com.ppb.kwid.R


class RegisterActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var btnRegister: Button
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        initUI()

    }

    private fun initUI() {
        etEmail = findViewById(R.id.et_register_email)
        etPassword = findViewById(R.id.et_register_password)
        tvError = findViewById(R.id.error_message)

        tvError.text = ""
        tvError.visibility = View.INVISIBLE


        btnRegister = findViewById(R.id.btn_register_submit)
        btnRegister.setOnClickListener {
            registerSubmit()
        }
    }

    private fun validateForm(email : String, password : String): Boolean {

        if (email.isNotBlank()) {
            if (password.isNotBlank()) {
                if (etPassword.text.length >= 6) {
//                    registerSubmit(email, password)

                    return true

                } else {
                    updateUI(null, "password must be 6 character or more")
                }
            } else {
                updateUI(null, "password cannot be null")
            }
        } else {
            updateUI(null, "email cannot be empty")
        }

        return false
    }

    private fun updateUI(user: FirebaseUser?, error: String?) {
        if (user != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            tvError.visibility = View.VISIBLE
            tvError.text = error
        }
    }


    private fun registerSubmit(){
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        if (validateForm(email,password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    override fun onComplete(task: Task<AuthResult?>) {
                        println("email is  " + email + " password is " + password)
                        if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                            Log.d("register success", "createUserWithEmail:success")
                            val user = mAuth.currentUser
                            updateUI(user, "")
                        } else { // If sign in fails, display a message to the user.
                            Log.w(
                                "register failur",
                                "createUserWithEmail:failure",
                                task.exception
                            )

                            Toast.makeText(applicationContext, "Input not valid", Toast.LENGTH_LONG)
                                .show()
                            updateUI(null, "The email is badly formatted")
                        }

                    }


                })


        }

    }

}
