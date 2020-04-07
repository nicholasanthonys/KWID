package com.ppb.kwid.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ppb.kwid.R


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {
    lateinit  var tv_overview : TextView
    companion object {
        fun newInstance(overview: String) : OverviewFragment {
            val fragment = OverviewFragment()
            val args = Bundle()
            args.putString("overview", overview)
            fragment.setArguments(args)
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

        tv_overview  = view.findViewById(R.id.movie_overview)
        tv_overview.text = this.arguments?.getString("overview")
    }



}
