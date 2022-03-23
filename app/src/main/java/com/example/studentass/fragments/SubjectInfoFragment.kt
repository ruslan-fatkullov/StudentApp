package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SubjectsRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.AnswerOfWorks
import com.example.studentass.models.SubjectOverview
import com.example.studentass.adapters.SubjectInfoRvAdapter
import kotlinx.android.synthetic.main.fragment_subject_info.*
import kotlinx.android.synthetic.main.fragment_subjects.*


class SubjectInfoFragment : Fragment() {
    companion object{
        var selected_item: String? = null
    }

    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        subjectLiteratureLayout.setOnClickListener(){
            // to Literature page
            selected_item = "Literature"
            getAppCompatActivity<MainActivity>()?.switchUp(LiteratureFragment::class.java)
        }
        subjectTaskLayout.setOnClickListener(){
            // to Task page
            //getAppCompatActivity<MainActivity>()?.switchUp(TaskFragment::class.java)
        }
        subjectTestLayout.setOnClickListener(){
            // to Test page
            selected_item = "Test"
            getAppCompatActivity<MainActivity>()?.switchUp(LiteratureFragment::class.java)
        }
        super.onHiddenChanged(false)
    }


    /*
     * Наполнение страницы элемнтами интерфейса
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_info, container, false)
    }



    /*
     * Управление заголовком страницы
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = SubjectsFragment.subName
        }
    }


}