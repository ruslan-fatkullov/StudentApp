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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.ArrayList

class LitertTaskTestFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var literatureList: List<LiteratureData>? = null
    private var themesListOf: List<PassedTests>? = null
    private val literatureApiService = LiteratureApiService.create()
    private val subService = SubjectApiService.create()
    public var currentLiterId: Long? = null

    companion object {
        var currentTest: PassedTests? = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        when (SubjectInfoFragment.selected_item) {
            "Literature" -> loadLiterarute()
            "Test" -> loadThemes()
            "Task" -> loadTask()
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


    private fun onGetIdsLiterature(
        literatureLis: List<LiteratureData>,
        adapter: LiteratureRvAdapter
    ) {
        literatureList = literatureLis
        adapter.dataList = literatureList as ArrayList<LiteratureData>

        adapter.setOnItemClickListener(object : LiteratureRvAdapter.OnItemClickListener {
            override fun setOnClickListener(position: Int) {
                currentLiterId = (literatureList as ArrayList<LiteratureData>).get(position).id
                Toast.makeText(context, currentLiterId.toString(), Toast.LENGTH_SHORT).show()
            }

        })

        adapter.notifyDataSetChanged()

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

    private fun onGetPassedTest(r: List<PassedTests>, adapter: TestRvAdapter) {
        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
        adapter.notifyDataSetChanged()
    }

    private fun onGetTask(taskList: List<TaskModel>) {
        if (taskList != null) {
            var adapter = literatureRv.adapter as TaskRvAdapter
            adapter.dataList = taskList as ArrayList<TaskModel>
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context, "nol`", Toast.LENGTH_SHORT).show()
        }

    }

    fun loadLiterarute() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = LiteratureRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragment.curSub?.id?.toLong()
        val userId = 2
        val adapter = literatureRv.adapter as LiteratureRvAdapter
        val disposableLiteratureListRx = literatureApiService
            .getIdLiterature(requestBody, subjectId, userId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetIdsLiterature(r, adapter) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableLiteratureListRx)

    }

    fun loadThemes() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TestRvAdapter(context!!)

        val requestBody = "Bearer " + LoginFragment.token
        val subjectId = SubjectsFragment.curSub?.id?.toLong()
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

    fun loadTask() {
        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TaskRvAdapter(context!!)

        val header = "Bearer " + LoginFragment.token
        val userId = SubjectsFragment.curSub?.teacherIds?.get(0)
        val jsonObject = JSONObject()
        jsonObject.put("key", "userId")
        jsonObject.put("operation", "==")
        jsonObject.put("value", userId)
        val jsonObjectString = "[$jsonObject]"
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        val disposableSubjectListRx = subService
            .getIdTask(header, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetTask(r) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    fun getDataOfTest(): ArrayList<TestQuestionModel> {
        val fileIds = arrayListOf<Long>()
        fileIds.add(2)
        val answers = arrayListOf<Any>()
        answers.add(3)
        answers.add("da")
        var dataOFQues = arrayListOf<TestQuestionModel>()
        dataOFQues.add(TestQuestionModel(1, "Вопрос 1", "WRITE", 1, fileIds, answers))
        dataOFQues.add(TestQuestionModel(2, "Вопрос 2", "SELECT", 1, fileIds, answers))
        dataOFQues.add(TestQuestionModel(3, "Вопрос 3", "MATCH", 1, fileIds, answers))
        return dataOFQues

    }


}


