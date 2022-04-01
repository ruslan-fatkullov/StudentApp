package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.R
import com.example.studentass.adapters.QuestionMatchRvAdapter
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.fragment_question_type_match.*
import kotlinx.android.synthetic.main.fragment_question_type_match.view.*


class QuestionTypeMatchFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        match_item_RV.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        match_item_RV.adapter = QuestionMatchRvAdapter(context!!)

        val question = view.matchTV
        question.text = TestFragment.currentQuestion?.question



        val adapter = match_item_RV.adapter as QuestionMatchRvAdapter
        adapter.dataList = TestFragment.currentQuestion?.answers as ArrayList<TestAnswer>


        adapter.setOnItemClickListener(object: QuestionMatchRvAdapter.onItemClickListener{
            override fun setOnClickListener(position: Int) {

            }

        })
        adapter.notifyDataSetChanged()

        super.onHiddenChanged(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_match, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//
//        getAppCompatActivity<MainActivity>()?.actionBar?.hide()
//
//    }

}