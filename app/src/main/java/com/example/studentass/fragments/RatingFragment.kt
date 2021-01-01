package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_rating.*

class RatingFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRatingGroupValue(35, 50)
        setRatingDepartmentValue(50, 120)
        setRatingUniversityValue(100, 1445)
        setAttendanceLectionsValue(33)
        setAttendancePracticesValue(75)

        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Рейтинг"
        }
    }

    private fun setRatingGroupValue(value: Int, maxValue: Int) {
        ratingGroupPb.max = maxValue
        ratingGroupPb.progress = value
        val tvText = "$value/$maxValue"
        ratingGroupTv.text = tvText
    }

    private fun setRatingDepartmentValue(value: Int, maxValue: Int) {
        ratingDepartmentPb.max = maxValue
        ratingDepartmentPb.progress = value
        val tvText = "$value/$maxValue"
        ratingDepartmentTv.text = tvText
    }

    private fun setRatingUniversityValue(value: Int, maxValue: Int) {
        ratingUniversityPb.max = maxValue
        ratingUniversityPb.progress = value
        val tvText = "$value/$maxValue"
        ratingUniversityTv.text = tvText
    }

    private fun setAttendanceLectionsValue(value: Int) {
        attendanceLectionsGg.value = value
        val tvText = "$value%"
        attendanceLectionsTv.text = tvText
    }

    private fun setAttendancePracticesValue(value: Int) {
        attendancePracticesGg.value = value
        val tvText = "$value%"
        attendancePracticesTv.text = tvText
    }
}