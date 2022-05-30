package com.example.studentass.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.studentass.R
import kotlinx.android.synthetic.main.fragment_question_type_write.*
import kotlinx.android.synthetic.main.fragment_question_type_write.view.*


class QuestionTypeWriteFragment : Fragment() {
    companion object {
        var questionId: Long? = null
        var anWindow: EditText? = null
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
        questionWriteBack.background = questionBack

        questionId = TestFragment.currentQuestion?.id

        val question = view.writeTv
        question.text = TestFragment.currentQuestion?.question

        anWindow = view.editTextWriteAnswer
        super.onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_question_type_write, container, false)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        questionId = null
        anWindow = null
    }

}