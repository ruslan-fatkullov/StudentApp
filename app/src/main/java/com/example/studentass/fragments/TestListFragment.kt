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
import com.example.studentass.adapters.TestRvAdapter
import com.example.studentass.fragments.SubjectInfoFragmentNew.Companion.listOfListTest
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.*
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import java.util.ArrayList

class TestListFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var themesListOf: List<PassedTests>? = null
    private val subService = SubjectApiService.create()

    companion object {
        var currentTest: PassedTests? = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TestRvAdapter(context!!)
        val adapter = literatureRv.adapter as TestRvAdapter


        val indexOfSubject = SubjectsFragmentNew.subjects?.indexOf(SubjectsFragmentNew.curSub)
        try {
            val testList = listOfListTest[indexOfSubject!!]
            setFragParams(adapter, testList)
            //Toast.makeText(context, "все ок", Toast.LENGTH_SHORT).show()
        } catch (e: IndexOutOfBoundsException) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            loadThemes(adapter, indexOfSubject)
        }



        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }


    private fun loadThemes(adapter: TestRvAdapter, indexOfSubject: Int?) {

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragmentNew.curSub?.id

        val disposableSubjectListRx = subService
            .getPassedTests(requestBody, subjectId, AccountFragment.currentUser.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetThemes(r, adapter, indexOfSubject) },
                { e ->
                    Toast.makeText(context, "Get test list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun onGetThemes(
        themesList: List<PassedTests>,
        adapter: TestRvAdapter,
        indexOfSubject: Int?
    ) {

        themesListOf = themesList
        if (indexOfSubject != null) {
            //Toast.makeText(context, themesList.toString(), Toast.LENGTH_SHORT).show()
            //Toast.makeText(context, "тесты добавлены", Toast.LENGTH_SHORT).show()
            listOfListTest.add(themesList)
            //Toast.makeText(context, listOfListTest[indexOfSubject].toString(), Toast.LENGTH_SHORT).show()

        }

        setFragParams(adapter, themesList)

    }

    private fun setFragParams(adapter: TestRvAdapter, themesList: List<PassedTests>) {
        themesListOf = themesList
        adapter.dataList = themesListOf as ArrayList<PassedTests>

        adapter.setOnItemClickListener(object : TestRvAdapter.onItemClickListener {
            override fun setOnClickListener(position: Int) {
                currentTest = (themesListOf as ArrayList<PassedTests>)[position]
                getAppCompatActivity<MainActivity>()?.actionBar?.hide()
                getAppCompatActivity<MainActivity>()?.switchUp(TestFragment::class.java)
            }

        })
        adapter.notifyDataSetChanged()

        if (themesList.isEmpty()) {
            contentAbsenceTv.visibility = View.VISIBLE
            contentAbsenceTv.text = "Тестов нет"
        }
    }


}


