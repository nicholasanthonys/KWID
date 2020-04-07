package com.ppb.kwid.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Model.Credits.Cast
import com.ppb.kwid.Model.Credits.CreditsAdapter
import com.ppb.kwid.Model.Credits.CreditsRepository
import com.ppb.kwid.Model.Credits.Crew

import com.ppb.kwid.R

/**
 * A simple [Fragment] subclass.
 */
class CastCrewFragment : Fragment() {
    private lateinit var rvCasts: RecyclerView
    private lateinit var creditAdapter: CreditsAdapter

    companion object {
        fun newInstance(movieID: Long) : CastCrewFragment {
            val fragment = CastCrewFragment()
            val args = Bundle()
            args.putLong("MOVIEID", movieID)
            fragment.setArguments(args)
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cast_crew, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCasts = view.findViewById(R.id.cast_and_crew)
        rvCasts.layoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        creditAdapter =
            CreditsAdapter(listOf(), listOf())
        rvCasts.adapter = creditAdapter

        val movieId = requireArguments().getLong("MOVIEID",0)
        CreditsRepository.getCasts(
            id = movieId,
            onSuccess = ::onCreditsFetched,
            onError = ::onError
        )
    }

    private fun onCreditsFetched(cast: List<Cast>, crews: List<Crew>) {
        creditAdapter.updateCasts(cast, crews)
    }
    private fun onError() {
        println("=========================================================")
        println("=======================ERRORR==================================")
        println("=========================================================")
    }
}
