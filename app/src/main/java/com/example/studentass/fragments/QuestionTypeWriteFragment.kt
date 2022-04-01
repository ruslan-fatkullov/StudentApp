package com.example.studentass.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.studentass.R
import com.example.studentass.fragments.LoginFragment.Companion.token
import com.example.studentass.models.testResultModel.testResult
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_question_type_write.*
import kotlinx.android.synthetic.main.fragment_question_type_write.view.*
import kotlinx.android.synthetic.main.fragment_test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class QuestionTypeWriteFragment : Fragment() {
    companion object{
        var questionId : Long? = null
        var anWindow : EditText? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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