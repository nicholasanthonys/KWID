package com.ppb.kwid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.ppb.kwid.Fragment.SeatListFragment
import com.ppb.kwid.Model.Seat.Seat
import com.ppb.kwid.Model.Seat.SelectedSeatAdapter
import com.ppb.kwid.Model.Transaction.Transaction
import com.ppb.kwid.R
import java.text.NumberFormat
import java.util.*

class SeatSelectionActivity : AppCompatActivity() {

    private lateinit var buttonRingkasanOrder: Button
    private lateinit var totalTicketPrice: TextView
    private lateinit var ticket: Transaction
    private lateinit var listOfSelectedSeat: MutableList<Seat>
    private lateinit var selectedSeatAdapter: SelectedSeatAdapter
    private lateinit var localeId: Locale
    private lateinit var formatRupiah: NumberFormat

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
        val seatColumns = 5
        val args = Bundle()
        args.putInt(SeatListFragment.SEAT_COLUMNS, seatColumns)

        val mFragmentManager = supportFragmentManager
        val seatListFragment = SeatListFragment()
        seatListFragment.arguments = args
        val fragment =
            supportFragmentManager.findFragmentByTag(SeatListFragment::class.java.simpleName)

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

        buttonRingkasanOrder = findViewById(R.id.btn_seat_selection)
        buttonRingkasanOrder.setOnClickListener {
            if (!listOfSelectedSeat.isEmpty()) {
                setTransactionData()

                val intent = Intent(this, PurchaseConfirmationActivity::class.java)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_TRANSACTION, ticket)
                intent.putExtra(PurchaseConfirmationActivity.EXTRA_IS_HISTORY, false)
                startActivity(intent)
            }
        }
    }

    private fun setTransactionData() {


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

        ticket = newTransaction
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
}
