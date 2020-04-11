package com.ppb.kwid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Model.TopUp.Bank
import com.ppb.kwid.Model.TopUp.BankAdapter
import com.ppb.kwid.Model.TopUp.CreditCard
import com.ppb.kwid.Model.TopUp.CreditCardAdapter
import com.ppb.kwid.R

class TopUpActivity : AppCompatActivity() {

    private lateinit var btnBackTopUp: ImageView
    private lateinit var recyclerCreditCard: RecyclerView
    private lateinit var recyclerBank: RecyclerView
    private var listOfBank: MutableList<Bank> = mutableListOf()
    private var listOfCreditCard: MutableList<CreditCard> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        getListOfBank()
        getListOfCreditCard()
        initUI()
    }

    private fun getListOfBank() {
        val arrayOfBankName = resources.getStringArray(R.array.bank_name)
        val arrayOfBankImageUrl = resources.getStringArray(R.array.bank_image_url)
        val arrayOfBankUrl = resources.getStringArray(R.array.bank_url)

        for (position in arrayOfBankName.indices) {
            val newBank = Bank(
                arrayOfBankName[position],
                arrayOfBankImageUrl[position],
                arrayOfBankUrl[position]
            )

            listOfBank.add(newBank)
        }
    }

    private fun getListOfCreditCard() {
        val arrayOfCreditCardName = resources.getStringArray(R.array.credit_card_name)
        val arrayOfCreditCardImageUrl = resources.getStringArray(R.array.credit_card_image_url)
        val arrayOfCreditCardUrl = resources.getStringArray(R.array.credit_card_url)

        for (position in arrayOfCreditCardName.indices) {
            val newCreditCard = CreditCard(
                arrayOfCreditCardName[position],
                arrayOfCreditCardImageUrl[position],
                arrayOfCreditCardUrl[position]
            )

            listOfCreditCard.add(newCreditCard)
        }
    }

    private fun initUI() {
        btnBackTopUp = findViewById(R.id.btn_back_top_up)
        btnBackTopUp.setOnClickListener {
            super.onBackPressed()
        }

        val bankLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerBank = findViewById(R.id.recyclerview_bank)
        recyclerBank.layoutManager = bankLinearLayoutManager
        val bankAdapter = BankAdapter(listOfBank)
        recyclerBank.adapter = bankAdapter

        val creditCardLinearLayoutManager: LinearLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerCreditCard = findViewById(R.id.recyclerview_credit_card)
        recyclerCreditCard.layoutManager = creditCardLinearLayoutManager
        val creditCardAdapter = CreditCardAdapter(listOfCreditCard)
        recyclerCreditCard.adapter = creditCardAdapter
    }
}
