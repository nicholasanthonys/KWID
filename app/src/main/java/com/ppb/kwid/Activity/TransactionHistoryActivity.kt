package com.ppb.kwid.Activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Model.Transaction.Transaction
import com.ppb.kwid.Model.Transaction.TransactionHistoryAdapter
import com.ppb.kwid.R

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerViewTransactionHistory: RecyclerView
    private lateinit var btnBackTransactionHitory: Button
    private lateinit var layoutManagerTransactionHistory: LinearLayoutManager
    private var listOfTransaction: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)

        getAllTransactionsData()
        initUI()
    }

    private fun getAllTransactionsData() {
        val arrayOfMoviePoster = resources.getStringArray(R.array.transaction_movie_poster)
        val arrayOfMoveTitle = resources.getStringArray(R.array.transaction_movie_title)
        val arrayOfCinemaName = resources.getStringArray(R.array.transaction_cinema_name)
        val arrayOfSchedule = resources.getStringArray(R.array.transaction_schedule)
        val arrayOfPickedSeat = resources.getStringArray(R.array.transaction_picked_seat)
        val arrayOfTicketPrice = resources.getStringArray(R.array.transaction_ticket_price)
        val arrayOfTime = resources.getStringArray(R.array.transaction_time)
        val arrayOfServiceFee = resources.getStringArray(R.array.transaction_service_fee)

        for (position in arrayOfMoviePoster.indices) {
            val seat: MutableList<String> = arrayOfPickedSeat[position].split(",").toMutableList()

            val newTransaction = Transaction(
                arrayOfMoviePoster[position],
                arrayOfMoveTitle[position],
                arrayOfCinemaName[position],
                arrayOfSchedule[position],
                seat,
                arrayOfTicketPrice[position].toInt(),
                arrayOfTime[position],
                arrayOfServiceFee[position].toInt()
            )

            listOfTransaction.add(newTransaction)
        }
    }

    private fun initUI() {
        btnBackTransactionHitory = findViewById(R.id.btn_back_transaction_history)
        btnBackTransactionHitory.setOnClickListener {
            super.onBackPressed()
        }

        recyclerViewTransactionHistory = findViewById(R.id.recyclerview_transaction_history)
        layoutManagerTransactionHistory =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTransactionHistory.layoutManager = layoutManagerTransactionHistory
        val transactionHistoryAdapter = TransactionHistoryAdapter(listOfTransaction, this)
        recyclerViewTransactionHistory.adapter = transactionHistoryAdapter
    }
}
