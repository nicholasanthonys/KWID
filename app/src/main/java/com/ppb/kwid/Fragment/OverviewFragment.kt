package com.ppb.kwid.Fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ppb.kwid.R


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    lateinit var tvOverview: TextView

    companion object {
        fun newInstance(overview: String): OverviewFragment {
            val fragment = OverviewFragment()
            val args = Bundle()
            args.putString("overview", overview)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvOverview = view.findViewById(R.id.movie_overview)
        tvOverview.text = this.arguments?.getString("overview")

        tvOverview.setOnTouchListener(OnTouchListener { v, event ->
            tvOverview.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        tvOverview.movementMethod = ScrollingMovementMethod()
    }


}
