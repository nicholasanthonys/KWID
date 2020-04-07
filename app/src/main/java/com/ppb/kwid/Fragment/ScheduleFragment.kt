package com.ppb.kwid.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ppb.kwid.R

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : Fragment() {
    companion object {
        fun newInstance(): ScheduleFragment {
            val fragment = ScheduleFragment()
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

}
