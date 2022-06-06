package com.example.studentass.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_test_result.view.*

class TestResultFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val testResultPercent = view.testResultPercent
        val backToTestBottom = view.backToTestBottom

        val resultTest = TestFragment.ratingOfTest.toString()
        testResultPercent.text = "${resultTest}%"

        backToTestBottom.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_result, container, false)
    }


}