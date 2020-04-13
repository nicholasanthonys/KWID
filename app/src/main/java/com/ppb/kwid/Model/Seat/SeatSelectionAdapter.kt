package com.ppb.kwid.Model.Seat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Fragment.SeatListFragment
import com.ppb.kwid.R

class SeatSelectionAdapter(
    private val seats: MutableList<Seat>,
    private val fragment: SeatListFragment
) :
    RecyclerView.Adapter<SeatSelectionAdapter.SeatSelectionListHolder>() {

    inner class SeatSelectionListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var button: Button

        fun bind(seat: Seat) {
            button = itemView.findViewById(R.id.background)
            button.text = seat.seatNumber

            if (seat.isEmpty) {

                if (seat.isSelected) {
                    button.isEnabled = true
                    button.isSelected = true
                    button.setTextColor(itemView.resources.getColor(R.color.colorWhite))
                } else {
                    button.isEnabled = true
                    button.isSelected = false
                    button.setTextColor(itemView.resources.getColor(R.color.colorWhite))
                }

            } else {
                button.isEnabled = false
                button.isClickable = false
                button.isSelected = false
                button.setTextColor(itemView.resources.getColor(R.color.colorBlack))
            }

            button.setOnClickListener {
                if (seat.isSelected) {
                    seat.isSelected = false
                    fragment.deleteSeat(seat)
                } else {
                    seat.isSelected = true
                    fragment.addSeat(seat)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatSelectionListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_seat, parent, false)

        return SeatSelectionListHolder(view)
    }

    override fun getItemCount(): Int {
        return seats.size
    }

    override fun onBindViewHolder(holder: SeatSelectionListHolder, position: Int) {
        holder.bind(seats[position])
    }
}