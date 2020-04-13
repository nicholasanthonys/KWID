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
import com.ppb.kwid.Model.Schedule.DatePicker
import com.ppb.kwid.Model.Schedule.DatePickerAdapter
import com.ppb.kwid.Model.Schedule.Schedule
import com.ppb.kwid.Model.Schedule.ScheduleAdapter
import com.ppb.kwid.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : Fragment() {

    private lateinit var date: DatePicker
    private lateinit var buttonLocation: LinearLayout
    private lateinit var buttonBuyTicket: LinearLayout
    private lateinit var recyclerViewDatePicker: RecyclerView
    private lateinit var recyclerViewTicketPicker: RecyclerView
    private var listOfDate: MutableList<DatePicker> = mutableListOf()
    private var listOfSchedule: MutableList<Schedule> = mutableListOf()
    private lateinit var ticketPickerAdapter: ScheduleAdapter

    //Variable for next activity
    private var moviePoster: String = ""
    private var movieName: String = ""
    private var cinemaName: String = ""
    private var schedule: String = ""
    private var ticketPrice: Int = 0
    private var time: String = ""

    companion object {
        fun newInstance(movieName: String, moviePoster: String): ScheduleFragment {
            val fragment = ScheduleFragment()
            val args = Bundle()
            args.putString("movieName", movieName)
            args.putString("moviePoster", moviePoster)
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
        ticketPickerAdapter = ScheduleAdapter(listOfSchedule)
        getDateData()

        buttonLocation = view.findViewById(R.id.button_location)
        buttonLocation.setOnClickListener {
            val intent = Intent(view.context, LocationActivity::class.java)
            view.context.startActivity(intent)
        }

        buttonBuyTicket = view.findViewById(R.id.btn_buy_ticket)
        buttonBuyTicket.setOnClickListener {
            getAllDataForTransaction()

            val intent = Intent(view.context, SeatSelectionActivity::class.java)

            //All data for transaction
            intent.putExtra(SeatSelectionActivity.EXTRA_MOVIE_POSTER, moviePoster)
            intent.putExtra(SeatSelectionActivity.EXTRA_MOVIE_NAME, movieName)
            intent.putExtra(SeatSelectionActivity.EXTRA_CINEMA_NAME, cinemaName)
            intent.putExtra(SeatSelectionActivity.EXTRA_SCHEDULE, schedule)
            intent.putExtra(SeatSelectionActivity.EXTRA_TICKET_PRICE, ticketPrice)
            intent.putExtra(SeatSelectionActivity.EXTRA_TIME, time)

            view.context.startActivity(intent)
        }

        recyclerViewDatePicker = view.findViewById(R.id.date_picker)
        val linearLayoutDatePicker =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDatePicker.layoutManager = linearLayoutDatePicker
        val datePickerAdapter = DatePickerAdapter(listOfDate, this)
        recyclerViewDatePicker.adapter = datePickerAdapter

        recyclerViewTicketPicker = view.findViewById(R.id.ticket_picker)
        val linearLayoutTicketPicker =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerViewTicketPicker.layoutManager = linearLayoutTicketPicker
        ticketPickerAdapter = ScheduleAdapter(listOfSchedule)
        recyclerViewTicketPicker.adapter = ticketPickerAdapter
    }

    fun setDate(newDate: DatePicker) {
        date = newDate
        getTicketData()
    }

    private fun getDateData() {
        val dateFormat = SimpleDateFormat("EEE.dd.MMM")
        val currentDate = Calendar.getInstance()

        for (i in 1..7) {
            val currentCal = dateFormat.format(currentDate.time).split('.')
            val day = currentCal[0]
            val date = currentCal[1] + " " + currentCal[2]

            var isEnabled = false
            var isSelected = false

            if (i < 3) {
                isEnabled = true
            }

            if (i == 1) {
                isSelected = true
            }

            val newDatePicker = DatePicker(
                date,
                day,
                isEnabled,
                isSelected,
                currentDate
            )

            listOfDate.add(newDatePicker)
            if (isSelected) {
                setDate(newDatePicker)
            }

            currentDate.add(Calendar.DATE, 1)
        }
    }

    private fun getTicketData() {
        //Seharusnya menggunakan date untuk mendapatkan tiket-tiket yang tersedia
        val arrayOfCinemaName = resources.getStringArray(R.array.schedule_cinema_name)
        val arrayOfCinemaType = resources.getStringArray(R.array.schedule_cinema_type)
        val arrayOfTicketPrice = resources.getStringArray(R.array.schedule_ticket_price)
        val arrayOfTime = resources.getStringArray(R.array.schedule_time)

        for (position in arrayOfCinemaName.indices) {
            val mutableListOfTime = arrayOfTime[position].split(',').toMutableList()

            val newSchedule = Schedule(
                arrayOfCinemaName[position],
                arrayOfCinemaType[position],
                arrayOfTicketPrice[position].toInt(),
                mutableListOfTime
            )

            listOfSchedule.add(newSchedule)
        }

        ticketPickerAdapter.updateData(listOfSchedule)
    }

    private fun getAllDataForTransaction() {
        moviePoster = this.arguments?.getString("moviePoster").toString()

        movieName = this.arguments?.getString("movieName").toString()

        cinemaName = ticketPickerAdapter.getSelectedCinemaName()

        val scheduleFormat = SimpleDateFormat("EEEE, dd MMM YYYY")
        schedule = scheduleFormat.format(date.calendar.time)

        ticketPrice = ticketPickerAdapter.getSelectedTicketPrice()

        time = ticketPickerAdapter.getSelectedTime()
    }
}
