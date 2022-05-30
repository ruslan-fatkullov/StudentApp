package com.example.studentass.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.R
import com.example.studentass.adapters.QuestionSequenceRvAdapter
import com.example.studentass.fragments.TestFragment.Companion.currentQuestion
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.fragment_question_type_sequence.*
import kotlinx.android.synthetic.main.fragment_question_type_sequence.view.*


class QuestionTypeSequenceFragment : Fragment() {

    companion object {
        var questionId: Long? = null
        var answer = arrayListOf<Long>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questionBack = DrawableCompat.wrap(context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.ic_question_back
            )
        }!!)
        DrawableCompat.setTintMode(questionBack, PorterDuff.Mode.SRC_ATOP)
        questionSequenceBack.background = questionBack

        questionId = currentQuestion?.id
        for (i in currentQuestion?.answers!!) {
            answer.add(i.id)
        }

        sequence_item_RV.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        sequence_item_RV.adapter = QuestionSequenceRvAdapter(context!!)

        val question = view.sequenceTV
        question.text = currentQuestion?.question


        val adapter = sequence_item_RV.adapter as QuestionSequenceRvAdapter
        adapter.dataList = currentQuestion?.answers as ArrayList<TestAnswer>



        adapter.notifyDataSetChanged()

        super.onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_question_type_sequence, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        questionId = null
        answer.clear()
    }
}