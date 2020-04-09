package com.ppb.kwid.Model.MovieDetail

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ppb.kwid.Fragment.CastCrewFragment
import com.ppb.kwid.Fragment.OverviewFragment
import com.ppb.kwid.Fragment.ScheduleFragment
import com.ppb.kwid.R

class MovieDetailsSectionsPageAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.overview, R.string.cast_crew, R.string.schedule)
    private var overview: String = ""
    private var movieId: Long = 0

    fun setAllParameters(overview: String, movieId: Long) {
        this.overview = overview
        this.movieId = movieId
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OverviewFragment.newInstance(overview)
            1 -> fragment = CastCrewFragment.newInstance(movieId)
            2 -> fragment = ScheduleFragment()
        }

        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}