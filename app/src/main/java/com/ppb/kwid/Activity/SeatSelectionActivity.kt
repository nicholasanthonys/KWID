package com.ppb.kwid.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Fragment.SeatListFragment
import com.ppb.kwid.Model.Seat.Seat
import com.ppb.kwid.Model.Seat.SelectedSeatAdapter
import com.ppb.kwid.Model.Transaction.Transaction
import com.ppb.kwid.R
import java.text.NumberFormat
import java.util.*

class SeatSelectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_POSTER = "extra_movie_poster"
        const val EXTRA_MOVIE_NAME = "extra_movie_name"
        const val EXTRA_CINEMA_NAME = "extra_cinema_name"
        const val EXTRA_SCHEDULE = "extra_schedule"
        const val EXTRA_TICKET_PRICE = "extra_ticket_price"
        const val EXTRA_TIME = "extra_time"
    }

    private lateinit var buttonRingkasanOrder: Button
    private lateinit var buttonBackSeatSelection: Button
    private lateinit var totalTicketPrice: TextView
    private lateinit var ticket: Transaction
    private lateinit var localeId: Locale
    private lateinit var formatRupiah: NumberFormat
    private lateinit var selectedSeatAdapter: SelectedSeatAdapter
    private lateinit var recyclerViewSelectedSeat: RecyclerView
    private var listOfSelectedSeat: MutableList<Seat> = mutableListOf()

    //For transaction data
    private var moviePoster: String = ""
    private var movieTitle: String = ""
    private var cinemaName: String = ""
    private var schedule: String = ""
    private var pickedSeat: MutableList<String> = mutableListOf()
    private var ticketPrice: Int = 20000
    private var time: String = ""
    private var serviceFee: Int = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_selection)

        initUI()
    }

    private fun initUI() {
        getTransactionData()

        val seatColumns = 5
        val args = Bundle()
        args.putInt(SeatListFragment.SEAT_COLUMNS, seatColumns)

        val mFragmentManager = supportFragmentManager
        val seatListFragment = SeatListFragment()
        seatListFragment.arguments = args
        val fragment = mFragmentManager.findFragmentByTag(SeatListFragment::class.java.simpleName)

        if (fragment !is SeatListFragment) {
            mFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_list_seat,
                    seatListFragment,
                    SeatListFragment::class.java.simpleName
                )
                .commit()
        }

        //For changing int to rupiah
        localeId = Locale("in", "ID")
        formatRupiah = NumberFormat.getCurrencyInstance()
        formatRupiah.maximumFractionDigits = 0
        formatRupiah.currency = Currency.getInstance(localeId)

        totalTicketPrice = findViewById(R.id.txt_total_price)
        totalTicketPrice.text = formatRupiah.format(0)

        recyclerViewSelectedSeat = findViewById(R.id.recyclerview_picked_seat)
        val linearLayoutSelectedSeat =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSelectedSeat.layoutManager = linearLayoutSelectedSeat
        selectedSeatAdapter = SelectedSeatAdapter(listOfSelectedSeat)
        recyclerViewSelectedSeat.adapter = selectedSeatAdapter


        buttonRingkasanOrder = findViewById(R.id.btn_seat_selection)
        buttonRingkasanOrder.setOnClickListener {
            if (!listOfSelectedSeat.isNullOrEmpty()) {
                setTransactionData()

                val intent = Intent(this, PurchaseConfirmationActivity::class.java)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_TRANSACTION, ticket)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_IS_HISTORY, false)
                startActivity(intent)
            }
        }

        buttonBackSeatSelection = findViewById(R.id.btn_back_seat_selection)
        buttonBackSeatSelection.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setTransactionData() {
        //listOfSelectedSeat.clear()
        for (selectedSeat in listOfSelectedSeat) {
            pickedSeat.add(selectedSeat.seatNumber)
        }

        val newTransaction = Transaction(
            moviePoster,
            movieTitle,
            cinemaName,
            schedule,
            pickedSeat,
            ticketPrice,
            time,
            serviceFee
        )
        println("dari seat selection " + newTransaction.picked_seat)

        ticket = newTransaction
        listOfSelectedSeat.clear()
    }

    private fun changeTotalTicketPrice() {
        val newTotalTicketPrice = listOfSelectedSeat.size * ticketPrice
        totalTicketPrice.text = formatRupiah.format(newTotalTicketPrice)
    }

    fun updateSelectedSeat(newListOfSelectedSeat: MutableList<Seat>) {
        listOfSelectedSeat = newListOfSelectedSeat

        selectedSeatAdapter.updateSelectedSeats(listOfSelectedSeat)
        selectedSeatAdapter.notifyDataSetChanged()

        changeTotalTicketPrice()
    }

    private fun getTransactionData() {
        moviePoster = intent.getStringExtra(EXTRA_MOVIE_POSTER)
        movieTitle = intent.getStringExtra(EXTRA_MOVIE_NAME)
        cinemaName = intent.getStringExtra(EXTRA_CINEMA_NAME)
        schedule = intent.getStringExtra(EXTRA_SCHEDULE)
        ticketPrice = intent.getIntExtra(EXTRA_TICKET_PRICE, 30000)
        time = intent.getStringExtra(EXTRA_TIME)
    }
}
