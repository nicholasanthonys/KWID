package com.ppb.kwid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ppb.kwid.Model.Transaction.Transaction
import com.ppb.kwid.R
import java.text.NumberFormat
import java.util.*

class PurchaseConfirmationActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TRANSACTION = "extra_transaction"
        const val EXTRA_IS_HISTORY = "extra_is_history"
    }

    private lateinit var appBarTitle: TextView
    private lateinit var moviePoster: ImageView
    private lateinit var movieTitle: TextView
    private lateinit var cinemaName: TextView
    private lateinit var schedule: TextView
    private lateinit var pickedSeat: TextView
    private lateinit var sumOfTicketPrice: TextView
    private lateinit var time: TextView
    private lateinit var totalOfSeat: TextView
    private lateinit var serviceFee: TextView
    private lateinit var totalPayment: TextView
    private lateinit var buttonPurchase: Button
    private var isHistory: Boolean = false
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_confirmation)

        getExtra()
        initUI()
    }

    private fun getExtra() {
        isHistory = intent.getBooleanExtra(EXTRA_IS_HISTORY, false)
        transaction = intent.getParcelableExtra(EXTRA_TRANSACTION)
    }

    private fun initUI() {
        appBarTitle = findViewById(R.id.appbar_title)
        moviePoster = findViewById(R.id.img_movie_poster)
        movieTitle = findViewById(R.id.txt_movie_title)
        cinemaName = findViewById(R.id.txt_cinema_name)
        schedule = findViewById(R.id.txt_schedule)
        pickedSeat = findViewById(R.id.txt_seat)
        sumOfTicketPrice = findViewById(R.id.sum_of_ticket_price)
        time = findViewById(R.id.txt_time)
        totalOfSeat = findViewById(R.id.sum_of_seat)
        serviceFee = findViewById(R.id.txt_service_fee)
        totalPayment = findViewById(R.id.total_payment)
        buttonPurchase = findViewById(R.id.btn_purchase_confirmation)

        //For changing int to rupiah
        val localeId = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance()
        formatRupiah.maximumFractionDigits = 0
        formatRupiah.currency = Currency.getInstance(localeId)

        if (isHistory) {
            val transactionDetail = resources.getString(R.string.transaction_detail)
            appBarTitle.text = transactionDetail

            buttonPurchase.visibility = View.GONE
        }

        Glide.with(this)
            .load(transaction.movie_poster)
            .centerCrop()
            .into(moviePoster)

        movieTitle.text = transaction.movie_title

        cinemaName.text = transaction.cinema_name

        schedule.text = transaction.schedule

        var allSeat = ""
        for (seat in transaction.picked_seat) {
            allSeat += "$seat, "
        }
        allSeat = allSeat.substring(0, allSeat.length - 2)
        pickedSeat.text = allSeat

        val intSumOfTicketPrice: Int = transaction.ticket_price * transaction.picked_seat.size
        sumOfTicketPrice.text = formatRupiah.format(intSumOfTicketPrice)

        time.text = transaction.time

        totalOfSeat.text = transaction.picked_seat.size.toString()

        serviceFee.text = formatRupiah.format(transaction.service_fee)

        val intTotalPayment: Int =
            intSumOfTicketPrice + (transaction.service_fee * transaction.picked_seat.size)
        totalPayment.text = formatRupiah.format(intTotalPayment)
    }
}
