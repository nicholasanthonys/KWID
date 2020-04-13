package com.ppb.kwid.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Activity.LocationActivity
import com.ppb.kwid.Activity.SeatSelectionActivity
import com.ppb.kwid.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : Fragment() {

    private lateinit var date: Date
    private lateinit var buttonLocation: LinearLayout
    private lateinit var buttonBuyTicket: LinearLayout
    private lateinit var recyclerViewDatePicker: RecyclerView
    private lateinit var recyclerViewTicketPicker: RecyclerView
    private var listOfDate: MutableList<Date> = mutableListOf()
    private var listOfTicketSchedule: MutableList<String> = mutableListOf()

    companion object {
        fun newInstance(movieID: Long): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putLong("MOVIEID", movieID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDateData()
        getTicketData()

        buttonLocation = view.findViewById(R.id.button_location)
        buttonLocation.setOnClickListener {
            val intent = Intent(view.context, LocationActivity::class.java)
            view.context.startActivity(intent)
        }

        buttonBuyTicket = view.findViewById(R.id.btn_buy_ticket)
        buttonBuyTicket.setOnClickListener {
            val intent = Intent(view.context, SeatSelectionActivity::class.java)
            view.context.startActivity(intent)
        }

        recyclerViewDatePicker = view.findViewById(R.id.date_picker)
        val linearLayoutDatePicker =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDatePicker.layoutManager = linearLayoutDatePicker


        recyclerViewTicketPicker = view.findViewById(R.id.ticket_picker)
        val linearLayoutTicketPicker =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewTicketPicker.layoutManager = linearLayoutTicketPicker
    }

    fun setDate(newDate: Date) {
        date = newDate
        getTicketData()
    }

    private fun getDateData() {

    }

    private fun getTicketData() {

    }
}
