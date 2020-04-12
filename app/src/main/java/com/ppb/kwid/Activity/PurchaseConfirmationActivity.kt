package com.ppb.kwid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppb.kwid.Model.Transaction.Transaction
import com.ppb.kwid.R

class PurchaseConfirmationActivity(
    private val transaction: Transaction,
    private val isHistory: Boolean
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_confirmation)
    }
}
