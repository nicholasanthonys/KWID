package com.ppb.kwid.Model.Schedule

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Fragment.ScheduleFragment
import com.ppb.kwid.R

class DatePickerAdapter(
    private var listOfDatePickers: MutableList<DatePicker>,
    private var fragment: ScheduleFragment
) :
    RecyclerView.Adapter<DatePickerAdapter.DatePickerListHolder>() {

    inner class DatePickerListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var button: Button

        fun bind(datePicker: DatePicker) {
            button = itemView.findViewById(R.id.background)

            var buttonText: String
            if (datePicker.is_enabled) {
                buttonText =
                    "<small> <font color='#A79E9E'>" + datePicker.date + "</font></small> <br> <font color='#000000'>" + datePicker.day + "</font>"
                button.isEnabled = true
            } else {
                buttonText =
                    "<small> <font color='#ECD2D2'>" + datePicker.date + "</font></small> <br> <font color='#ECD2D2'>" + datePicker.day + "</font>"
                button.isEnabled = false
            }

            if (datePicker.is_selected) {
                buttonText =
                    "<small> <font color='#ECD2D2'>" + datePicker.date + "</font></small> <br> <font color='#FFFFFF'>" + datePicker.day + "</font>"
                button.isSelected = true
            } else {
                button.isSelected = false
            }

            button.text = Html.fromHtml(buttonText)

            button.setOnClickListener {
                if (!datePicker.is_selected) {
                    fragment.setDate(datePicker)

                    changeAllIsSeletedToFalse()
                    datePicker.is_selected = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatePickerListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_date, parent, false)

        return DatePickerListHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfDatePickers.size
    }

    override fun onBindViewHolder(holder: DatePickerListHolder, position: Int) {
        holder.bind(listOfDatePickers[position])
    }

    private fun changeAllIsSeletedToFalse() {
        for (datePicker in listOfDatePickers) {
            datePicker.is_selected = false
        }
    }
}