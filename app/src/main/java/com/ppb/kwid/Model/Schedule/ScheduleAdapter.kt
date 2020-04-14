package com.ppb.kwid.Model.Schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(private var schedules: MutableList<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleListHolder>() {

    private var time: String = ""
    private var cinemaName: String = ""
    private var ticketPrice: Int = 0
    private var arrayOfButton: MutableList<Button> = mutableListOf()
    private var arrayOfCinemaName: MutableList<String> = mutableListOf()
    private var arrayOfLinearLayout: MutableList<LinearLayout> = mutableListOf()

    inner class ScheduleListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var textCinemaName: TextView
        private lateinit var textCinemaType: TextView
        private lateinit var textTicketPrice: TextView
        private lateinit var linearLayoutTime: LinearLayout

        fun bind(schedule: Schedule) {
            textCinemaName = itemView.findViewById(R.id.txt_mall)
            textCinemaName.text = schedule.cinema_name

            textCinemaType = itemView.findViewById(R.id.txt_type)
            textCinemaType.text = schedule.cinema_type


            //For changing int to rupiah
            val localeId = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance()
            formatRupiah.maximumFractionDigits = 0
            formatRupiah.currency = Currency.getInstance(localeId)
            textTicketPrice = itemView.findViewById(R.id.txt_price)
            textTicketPrice.text = formatRupiah.format(schedule.ticket_price)

            val timeFormat = SimpleDateFormat("HH.mm")
            val currentTime = timeFormat.format(Calendar.getInstance().time)
            linearLayoutTime = itemView.findViewById(R.id.time_picker)
            arrayOfLinearLayout.add(linearLayoutTime)

            //Add button to linear layout
            for (position in schedule.time.indices) {
                val newButtonTime = Button(itemView.context)
                newButtonTime.text = schedule.time[position]
                newButtonTime.background = itemView.resources.getDrawable(R.drawable.button_time)
                newButtonTime.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setMarginsInDp(newButtonTime, 5, 0, 5, 0)

                if (currentTime.toDouble() > schedule.time[position].toDouble()) {
                    newButtonTime.isEnabled = false
                    newButtonTime.isClickable = false
                } else {
                    newButtonTime.isEnabled = true
                    newButtonTime.isClickable = true

                    if (cinemaName.equals("") and time.equals("")) {
                        cinemaName = schedule.cinema_name
                        time = schedule.time[position]
                        ticketPrice = schedule.ticket_price
                    }

                    newButtonTime.setOnClickListener {
                        cinemaName = schedule.cinema_name
                        time = schedule.time[position]
                        ticketPrice = schedule.ticket_price
                        updateButtonCondition(itemView)
                    }
                }

                newButtonTime.isSelected = false
                if (schedule.cinema_name.equals(cinemaName) and schedule.time[position].equals(time)) {
                    newButtonTime.isSelected = true
                } else {
                    newButtonTime.setTextColor(itemView.resources.getColor(R.color.colorBlack))
                }

                linearLayoutTime.addView(newButtonTime)
                arrayOfButton.add(newButtonTime)
                arrayOfCinemaName.add(schedule.cinema_name)
            }
        }
    }

    private fun updateButtonCondition(itemView: View) {
        for (position in arrayOfButton.indices) {
            if (arrayOfButton[position].isEnabled) {
                if (arrayOfCinemaName[position].equals(cinemaName) and arrayOfButton[position].text.equals(
                        time
                    )
                ) {
                    arrayOfButton[position].isSelected = true
                    arrayOfButton[position].setTextColor(itemView.resources.getColor(R.color.colorWhite))
                } else {
                    arrayOfButton[position].isSelected = false
                    arrayOfButton[position].setTextColor(itemView.resources.getColor(R.color.colorBlack))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)

        return ScheduleListHolder(view)
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    override fun onBindViewHolder(holder: ScheduleListHolder, position: Int) {
        holder.bind(schedules[position])
    }

    fun setMarginsInDp(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val screenDesity: Float = view.context.resources.displayMetrics.density
            val params: ViewGroup.MarginLayoutParams =
                view.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(
                left * screenDesity.toInt(),
                top * screenDesity.toInt(),
                right * screenDesity.toInt(),
                bottom * screenDesity.toInt()
            )
            view.requestLayout()
        }
    }

    fun updateData(schedules: MutableList<Schedule>) {
        this.schedules = schedules

        //Delete all button
        for (linearLayout in arrayOfLinearLayout) {
            linearLayout.removeAllViews()
        }

        //Setup data
        time = ""
        cinemaName = ""
        ticketPrice = 0
        arrayOfButton.clear()
        arrayOfCinemaName.clear()
        arrayOfLinearLayout.clear()

        notifyDataSetChanged()
    }

    fun getSelectedCinemaName(): String {
        return cinemaName
    }

    fun getSelectedTime(): String {
        return time
    }

    fun getSelectedTicketPrice(): Int {
        return ticketPrice
    }
}