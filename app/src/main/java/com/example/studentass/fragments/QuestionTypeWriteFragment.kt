package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_question_type_select.view.*
import kotlinx.android.synthetic.main.fragment_question_type_write.*
import kotlinx.android.synthetic.main.fragment_question_type_write.view.*


class QuestionTypeWriteFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val question = view.writeTV
        question.text = TestFragment.currentQuestion?.question

        super.onHiddenChanged(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_write, container, false)
    }



//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//
//        getAppCompatActivity<MainActivity>()?.actionBar?.hide()
//    }

}