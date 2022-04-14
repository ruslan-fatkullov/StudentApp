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
import com.example.studentass.adapters.TaskRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.*
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_literature.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.ArrayList

class TaskListFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private val subService = SubjectApiService.create()

    companion object {
        lateinit var currentTask: TaskModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        literatureRv.layoutManager =
            LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        literatureRv.adapter = TaskRvAdapter(context!!)

        val adapter = literatureRv.adapter as TaskRvAdapter


        val indexOfSubject = SubjectsFragmentNew.subjects?.indexOf(SubjectsFragmentNew.curSub)
        try {
            val testList = SubjectInfoFragmentNew.listOfListTask[indexOfSubject!!]
            setFragParams(adapter, testList)
        } catch (e: IndexOutOfBoundsException) {
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            loadTask(indexOfSubject, adapter)
        }

        //loadTask(indexOfSubject, adapter)

        onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literature, container, false)
    }


    private fun loadTask(indexOfSubject: Int?, adapter: TaskRvAdapter) {

        val header = "Bearer " + LoginFragment.token
        val userId = SubjectsFragmentNew.curSub?.teacherIds?.get(0)
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
                { r -> onGetTask(r, adapter, indexOfSubject) },
                { e ->
                    Toast.makeText(context, "Get literature list error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun onGetTask(taskList: List<TaskModel>, adapter: TaskRvAdapter, indexOfSubject: Int?) {

        if (indexOfSubject != null) {
            SubjectInfoFragmentNew.listOfListTask.add(taskList)
        }
        setFragParams(adapter, taskList)
    }


    private fun setFragParams(adapter: TaskRvAdapter, taskList: List<TaskModel>) {
        adapter.dataList = taskList as ArrayList<TaskModel>
        adapter.setonFocusChangeListener(object : TaskRvAdapter.onFocusChangeListener {
            override fun setOnClickListener(position: Int) {
                currentTask = taskList[position]
                getAppCompatActivity<MainActivity>()?.switchUp(TaskFragment::class.java)
            }

        })
        adapter.notifyDataSetChanged()
        if (taskList.isEmpty()) {
            contentAbsenceTv.visibility = View.VISIBLE
            contentAbsenceTv.text = "Заданий нет"
        }
    }

}


