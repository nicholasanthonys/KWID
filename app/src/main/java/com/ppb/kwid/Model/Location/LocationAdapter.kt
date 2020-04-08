package com.ppb.kwid.Model.Location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.R

class LocationAdapter(private val listLocation: MutableList<String>) :
    RecyclerView.Adapter<LocationAdapter.LocationListHolder>() {
    class LocationListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(locationName: String) {
            val txtLocation: TextView = itemView.findViewById(R.id.txt_location)
            txtLocation.text = locationName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return LocationListHolder(view)
    }

    override fun getItemCount(): Int = listLocation.size

    override fun onBindViewHolder(holder: LocationListHolder, position: Int) {
        holder.bind(listLocation[position])
    }
}