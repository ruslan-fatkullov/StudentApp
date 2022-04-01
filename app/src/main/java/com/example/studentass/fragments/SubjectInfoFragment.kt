package com.example.studentass.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_subject_info.*
import kotlinx.android.synthetic.main.fragment_subject_info.view.*


class SubjectInfoFragment : Fragment() {
    companion object{
        var selected_item: String? = null
    }


    /*
     * Инициализация элементов интерфейса
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val width: Int = display.width
        val subjectTaskLayout = view.subjectTaskLayout
        val subjectLiteratureLayout = view.subjectLiteratureLayout
        val subjectTestLayout = view.subjectTestLayout

        subjectTaskLayout.layoutParams.width = width / 3
        subjectLiteratureLayout.layoutParams.width = width / 3
        subjectTestLayout.layoutParams.width = width / 3


        subjectLiteratureLayout.setOnClickListener(){
            // to Literature page
            selected_item = "Literature"
            getAppCompatActivity<MainActivity>()?.switchUp(LitertTaskTestFragment::class.java)
        }
        subjectTaskLayout.setOnClickListener(){
            // to Task page
            selected_item = "Task"
            getAppCompatActivity<MainActivity>()?.switchUp(LitertTaskTestFragment::class.java)
        }
        subjectTestLayout.setOnClickListener(){
            // to Test page
            selected_item = "Test"
            getAppCompatActivity<MainActivity>()?.switchUp(LitertTaskTestFragment::class.java)
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

//        getAppCompatActivity<MainActivity>()?.actionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.task_background))
//        getAppCompatActivity<MainActivity>()?.actionBar?.show()

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = SubjectsFragment.curSub?.name
        }
    }


}