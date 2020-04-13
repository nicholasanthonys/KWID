package com.ppb.kwid.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Activity.SeatSelectionActivity
import com.ppb.kwid.Model.Seat.Seat
import com.ppb.kwid.Model.Seat.SeatSelectionAdapter

import com.ppb.kwid.R

/**
 * A simple [Fragment] subclass.
 */
class SeatListFragment : Fragment() {

    companion object {
        const val SEAT_COLUMNS = "seat_columns"
    }

    private lateinit var recyclerViewSeatList: RecyclerView
    private lateinit var listOfSeat: MutableList<Seat>
    private lateinit var listOfSelectedSeat: MutableList<Seat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seatColumns = requireArguments().getInt(SEAT_COLUMNS)
        getSeatList()

        recyclerViewSeatList = view.findViewById(R.id.recyclerview_seat_list)
        val gridLayoutManger = GridLayoutManager(view.context, seatColumns)
        recyclerViewSeatList.layoutManager = gridLayoutManger
        val seatListAdapter = SeatSelectionAdapter(listOfSeat, this)
        recyclerViewSeatList.adapter = seatListAdapter
    }

    private fun getSeatList() {
        val seats = resources.getString(R.string.dummy_seat_selection).split(',')
        val isEmptys = resources.getString(R.string.dummy_is_empty).split(',')

        for (position in seats.indices) {
            val newSeat = Seat(
                seats[position],
                isEmptys[position].toBoolean()
            )

            listOfSeat.add(newSeat)
        }
    }

    fun addSeat(newSeat: Seat) {
        listOfSelectedSeat.add(newSeat)
        (activity as SeatSelectionActivity).updateSelectedSeat(listOfSelectedSeat)
    }

    fun deleteSeat(selectedSeat: Seat) {
        listOfSelectedSeat.remove(selectedSeat)
        (activity as SeatSelectionActivity).updateSelectedSeat(listOfSelectedSeat)
    }
}
