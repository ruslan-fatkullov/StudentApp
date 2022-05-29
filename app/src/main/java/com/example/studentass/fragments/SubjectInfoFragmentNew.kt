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
import com.example.studentass.fragments.SubjectsFragmentNew.Companion.curSub
import com.example.studentass.fragments.SubjectsFragmentNew.Companion.subjects
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.PassedTests
import com.example.studentass.models.TaskModel
import kotlinx.android.synthetic.main.fragment_subject_info_new.*
import kotlinx.android.synthetic.main.liter_task_test_item.view.*
import kotlinx.android.synthetic.main.subjects_overview_item_new.view.*
import kotlin.random.Random


class SubjectInfoFragmentNew : Fragment() {
    private lateinit var sfm: FragmentManager
    private var literFragment: Fragment? = null
    private var taskFragment: Fragment? = null
    private var testFragment: Fragment? = null
    private var currentFrag: Fragment? = null
    private var currentFragID: Int? = 1

    companion object {
        var listOfListTest = arrayListOf<List<PassedTests>>()
        var listOfListTask = arrayListOf<List<TaskModel>>()
        var listOfListLiterature = arrayListOf<List<LiteratureData>>()
    }


    /*
     * Инициализация элементов интерфейса
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val maxSub = subjects?.size
        val numOfCurrentSub = "1"
        //subOverView.countOfSub.text = "$numOfCurrentSub/$maxSub"

        val nextValues = subjects?.size?.let { List(it) { Random.nextInt(0, 3) } }
        //val ran = Random.nextInt(0,3)
        var subjectBackground = nextValues?.get(0)?.let { background_for_subject(it) }
        subOverView.subItemCL.background = subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }

        subOverView.nameSub.text = curSub?.name
        if (subjects?.size == 1) {
            nextSubBtn.visibility = View.INVISIBLE
        }
        prevSubBtn.visibility = View.INVISIBLE

        subOverView.subItemCL.elevation = 15.0F

        subject_info_layout.background =
            ContextCompat.getDrawable(context!!, R.drawable.actionbar_background_rectangle)

        subjectLiteratureLayout.bookTv.text = "Литература"
        subjectTaskLayout.bookTv.text = "Работы"
        subjectTestLayout.bookTv.text = "Тесты"
        subjectLiteratureLayout.bookIv.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_liter)
        subjectTaskLayout.bookIv.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_articles)
        subjectTestLayout.bookIv.background =
            ContextCompat.getDrawable(context!!, R.drawable.ic_projects)


        loadContent()
        nextSubBtn.setOnClickListener {
            prevSubBtn.visibility = View.VISIBLE
            val ind = subjects?.indexOf(curSub)
            if (ind != null) {
                subjectBackground = nextValues?.get(ind)?.let { background_for_subject(it) }
                subOverView.subItemCL.background = subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }
                curSub = subjects?.get(ind + 1)
                //subOverView.countOfSub.text = "${ind+2}/$maxSub"
                subOverView.nameSub.text = curSub?.name
                if (ind == subjects?.size?.minus(2)) {
                    nextSubBtn.visibility = View.INVISIBLE
                }
            }
            loadContent()
        }
        prevSubBtn.setOnClickListener {
            nextSubBtn.visibility = View.VISIBLE
            val ind = subjects?.indexOf(curSub)
            if (ind != null) {
                subjectBackground = nextValues?.get(ind)?.let { background_for_subject(it) }
                subOverView.subItemCL.background = subjectBackground?.let { ContextCompat.getDrawable(context!!, it) }

                curSub = subjects?.get(ind - 1)
                //subOverView.countOfSub.text = "${ind}/$maxSub"
                subOverView.nameSub.text = curSub?.name
                if (ind == 1) {
                    prevSubBtn.visibility = View.INVISIBLE
                }
            }
            loadContent()

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
        return inflater.inflate(R.layout.fragment_subject_info_new, container, false)
    }

    private fun loadContent() {
        testFragment?.let { sfm.beginTransaction().remove(it).commit() }
        taskFragment?.let { sfm.beginTransaction().remove(it).commit() }
        literFragment?.let { sfm.beginTransaction().remove(it).commit() }

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

        currentFrag = when (currentFragID) {
            1 -> taskFragment
            2 -> literFragment
            else -> testFragment
        }
        currentFrag?.let { sfm.beginTransaction().show(it).commit() }

        subjectLiteratureLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = literFragment
            currentFragID = 2
            sfm.beginTransaction().show(literFragment as LiteratureFragment).commit()
        }
        subjectTaskLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = taskFragment
            currentFragID = 1
            sfm.beginTransaction().show(taskFragment as TaskListFragment).commit()
        }
        subjectTestLayout.setOnFocusChangeListener { _, _ ->
            currentFrag?.let { it1 -> sfm.beginTransaction().hide(it1).commit() }
            currentFrag = testFragment
            currentFragID = 3
            sfm.beginTransaction().show(testFragment as TestListFragment).commit()

        }
    }

    private fun background_for_subject(ran: Int): Int{
        return when(ran){
            0 -> R.drawable.subject_background_rectangl_blue
            1 -> R.drawable.subject_background_rectangl_orange
            2 -> R.drawable.subject_background_rectangl_purple
            else -> {R.drawable.subject_background_rectangl_purple}
        }
    }

}