package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.LiteratureRvAdapter
import com.example.studentass.adapters.TaskRvAdapter
import com.example.studentass.adapters.TestRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.*
import com.example.studentass.services.LiteratureApiService
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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

        loadThemes()
        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }




    private fun onGetThemes(
        themesList: List<PassedTests>,
        adapter: TestRvAdapter,
        requestBody: String,
        subjectId: Long?
    ) {

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
    }



    fun loadThemes() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TestRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragmentNew.curSub?.id?.toLong()
        //val subjectId: Long = 1
        val adapter = literatureRv.adapter as TestRvAdapter
        val disposableSubjectListRx = subService
            .getPassedTests(requestBody, subjectId, AccountFragment.currentUser.id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetThemes(r, adapter, requestBody, subjectId) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }



}


