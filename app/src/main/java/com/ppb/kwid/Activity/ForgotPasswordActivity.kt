package com.ppb.kwid.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ppb.kwid.R

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var tvMessage: TextView
    private lateinit var etEmail: EditText
    private lateinit var btnReset: Button
    private lateinit var btnBack: Button

    //firebase instance
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mAuth = FirebaseAuth.getInstance()
        initUI()
    }

    private fun initUI() {
        tvMessage = findViewById(R.id.tv_forgotpassword_message)
        etEmail = findViewById(R.id.et_forgotpassword_email)
        btnReset = findViewById(R.id.btn_forgot_password_submit)
        btnBack = findViewById(R.id.btn_back_forgot_password)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnReset.setOnClickListener {
            sendEmail()
        }

    }

    private fun updateMessage(message: String) {
        if (message.isNotEmpty()) {
            if (message == "Email Sent. Please Check Your Email") {
                val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra(MESSAGE, resources.getString(R.string.email_sent_forgot_password))
                startActivity(intent)
            }else{
                tvMessage.text = message
                tvMessage.setPadding(16, 10, 16, 10)
            }

        }
    }

    private fun sendEmail() {
        val emailAddress = etEmail.text.toString()

        if (emailAddress.isNotEmpty()) {
            mAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Email sent", "Email sent.")
                        var message = "Email Sent. Please Check Your Email"
                        updateMessage(message)
                    } else {
                        var message = "Sending email failed"
                        updateMessage(message)
                    }
                }
        }
        else{
            updateMessage("Email is empty")
        }


    }
}
