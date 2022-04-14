package com.example.studentass.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.fragments.SubjectsFragment.Companion.curSub
import com.example.studentass.getAppCompatActivity
import kotlinx.android.synthetic.main.fragment_subject_info.*
import kotlinx.android.synthetic.main.liter_task_test_item.view.*


class SubjectInfoFragment : Fragment() {
    private lateinit var sfm: FragmentManager
    private var literFragment : Fragment? = null
    private var taskFragment : Fragment? = null
    private var testFragment : Fragment? = null
    private var currentFrag : Fragment? = null
    companion object{
        var selected_item: String? = null
    }


    /*
     * Инициализация элементов интерфейса
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameSub.text = curSub?.name
        subjectLiteratureLayout.bookTv.text = "Литература"
        subjectTaskLayout.bookTv.text = "Работы"
        subjectTestLayout.bookTv.text = "Тесты"
        subjectLiteratureLayout.bookIv.background = ContextCompat.getDrawable(context!!, R.drawable.ic_liter)
        subjectTaskLayout.bookIv.background = ContextCompat.getDrawable(context!!, R.drawable.ic_articles)
        subjectTestLayout.bookIv.background = ContextCompat.getDrawable(context!!, R.drawable.ic_projects)

        sfm = getAppCompatActivity<MainActivity>()!!.createFragmentManagerForSubject()

        testFragment = TestListFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, testFragment!!).commit()
        sfm.beginTransaction().hide(testFragment as TestListFragment).commit()

        taskFragment = TaskListFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, taskFragment!!).commit()
        sfm.beginTransaction().hide(taskFragment as TaskListFragment).commit()

        literFragment = LiteratureFragment::class.java.newInstance()
        sfm.beginTransaction().add(subject_content_RV.id, literFragment!!).commit()
        sfm.beginTransaction().hide(literFragment as LiteratureFragment).commit()

        currentFrag = taskFragment
        currentFrag?.let { sfm.beginTransaction().show(it).commit() }

        subjectLiteratureLayout.setOnFocusChangeListener{ _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = literFragment
            sfm.beginTransaction().show(literFragment as LiteratureFragment).commit()
        }
        subjectTaskLayout.setOnFocusChangeListener(){ _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = taskFragment
            sfm.beginTransaction().show(taskFragment as TaskListFragment).commit()
        }
        subjectTestLayout.setOnFocusChangeListener(){ _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = testFragment
            sfm.beginTransaction().show(testFragment as TestListFragment).commit()

        }
//        subjectLiteratureLayout.setOnClickListener(){
//            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
//            currentFrag = literFragment
//            sfm.beginTransaction().show(literFragment as LiteratureFragment).commit()
//        }
//        subjectTaskLayout.setOnClickListener(){
//            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
//            currentFrag = taskFragment
//            sfm.beginTransaction().show(taskFragment as TaskFragment).commit()
//        }
//        subjectTestLayout.setOnClickListener(){
//            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
//            currentFrag = testFragment
//            sfm.beginTransaction().show(testFragment as TestListFragment).commit()
//
//        }


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



//    /*
//     * Управление заголовком страницы
//     */
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//
////        getAppCompatActivity<MainActivity>()?.actionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.task_background))
////        getAppCompatActivity<MainActivity>()?.actionBar?.show()
//
//
//    }


}