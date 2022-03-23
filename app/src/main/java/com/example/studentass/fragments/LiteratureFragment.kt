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
import com.example.studentass.adapters.LiteratureRvAdapter
import com.example.studentass.adapters.TestRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.TestThemesData
import com.example.studentass.services.LiteratureApiService
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import java.util.ArrayList

class LiteratureFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var literatureList: List<LiteratureData>? = null
    private var themesListOf: List<TestThemesData>? = null
    private val literatureApiService = LiteratureApiService.create()
    private val subService = SubjectApiService.create()


    fun loadLiterarute(){
        literatureRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = LiteratureRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragment.subID?.toLong()
        val userId = 2
        val adapter = literatureRv.adapter as LiteratureRvAdapter
        val disposableSubjectListRx = literatureApiService
            .getIdLiterature(requestBody, subjectId , userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetIdsLiterature(r, adapter)},
                {e -> Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    fun loadTests(){
        literatureRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TestRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragment.subID?.toLong()
        val userId = 2
        val adapter = literatureRv.adapter as TestRvAdapter
        val disposableSubjectListRx = subService
            .getIdThemes(requestBody, subjectId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onGetThemes(r, adapter)},
                {e -> Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(SubjectInfoFragment.selected_item){
            "Literature" -> loadLiterarute()
            "Test" -> loadTests()
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Список литературы"
        }
    }

    private fun onGetIdsLiterature(literatureLis: List<LiteratureData>, adapter: LiteratureRvAdapter){
        literatureList = literatureLis
        adapter.dataList = literatureList as ArrayList<LiteratureData>
        adapter.notifyDataSetChanged()
    }

    private fun onGetThemes(themesList: List<TestThemesData>, adapter: TestRvAdapter){
        themesListOf = themesList
        adapter.dataList = themesListOf as ArrayList<TestThemesData>
        adapter.notifyDataSetChanged()
    }

}