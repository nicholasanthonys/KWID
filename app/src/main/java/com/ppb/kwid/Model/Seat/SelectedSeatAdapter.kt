package com.ppb.kwid.Model.Seat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.R

class SelectedSeatAdapter(private var selectedSeats: MutableList<Seat>) :
    RecyclerView.Adapter<SelectedSeatAdapter.SelectedSeatListHolder>() {

    inner class SelectedSeatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var button: Button

        fun bind(selectedSeat: Seat) {
            button = itemView.findViewById(R.id.background)
            button.text = selectedSeat.seatNumber

            button.isSelected = true
            button.isEnabled = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedSeatListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_seat, parent, false)

        return SelectedSeatListHolder(view)
    }

    override fun getItemCount(): Int {
        return selectedSeats.size
    }

    override fun onBindViewHolder(holder: SelectedSeatListHolder, position: Int) {
        holder.bind(selectedSeats[position])
    }

    fun updateSelectedSeats(selectedSeats: MutableList<Seat>) {
        this.selectedSeats = selectedSeats
    }
}