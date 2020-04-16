package com.ppb.kwid.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ppb.kwid.Activity.ReviewActivity
import com.ppb.kwid.R


/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    lateinit var tvOverview: TextView
    private lateinit var buttonOverview: Button

    companion object {
        fun newInstance(overview: String, title: String, poster: String): OverviewFragment {
            val fragment = OverviewFragment()
            val args = Bundle()
            args.putString("overview", overview)
            args.putString("title", title)
            args.putString("poster", poster)
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

//        tvOverview.setOnTouchListener(OnTouchListener { v, event ->
//            tvOverview.parent.requestDisallowInterceptTouchEvent(true)
//            false
//        })
//
//        tvOverview.movementMethod = ScrollingMovementMethod()

        buttonOverview = view.findViewById(R.id.button_review)
        buttonOverview.setOnClickListener {
            val intent = Intent(view.context, ReviewActivity::class.java)
            intent.putExtra(ReviewActivity.TITLE, this.arguments?.getString("title"))
            intent.putExtra(ReviewActivity.POSTER, this.arguments?.getString("poster"))
            view.context.startActivity(intent)
        }
    }


}
