package com.example.studentass.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.R
import com.example.studentass.adapters.QuestionSelectRvAdapter
import com.example.studentass.fragments.TestFragment.Companion.ratingOfTest
import com.example.studentass.fragments.TestFragment.Companion.requestToCheckTest
import com.example.studentass.models.TestAnswer
import com.example.studentass.models.testResultModel.testResult
import kotlinx.android.synthetic.main.fragment_question_type_select.*
import kotlinx.android.synthetic.main.fragment_question_type_select.view.*


class QuestionTypeSelectFragment : Fragment() {

    companion object{
        var questionId : Long? = null
        var answ = arrayListOf<Long>()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        questionId = TestFragment.currentQuestion?.id


        select_item_RV.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        select_item_RV.adapter = QuestionSelectRvAdapter(context!!)

        val question = view.selectTV
        question.text = TestFragment.currentQuestion?.question


        val adapter = select_item_RV.adapter as QuestionSelectRvAdapter
        adapter.dataList = TestFragment.currentQuestion?.answers as ArrayList<TestAnswer>
        var siz = TestFragment.currentQuestion?.answers?.size
        for (item in 0..siz!!){
            adapter.clicked.add(false)
        }


        adapter.notifyDataSetChanged()

        super.onHiddenChanged(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_select, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        questionId = null
        answ.clear()

    }

    fun onCheckTest(context: Context?, t: testResult) {
        Log.d("rating",t.rating.toString())
        ratingOfTest = t.rating
    }


}