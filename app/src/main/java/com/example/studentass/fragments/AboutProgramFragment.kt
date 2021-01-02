package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_about_program.*

class AboutProgramFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitAboutProgramBn.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }

        onHiddenChanged(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        onHiddenChanged(true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.hide()
        }
        else {
            getAppCompatActivity<MainActivity>()?.actionBar?.show()
        }
    }
}