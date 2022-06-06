package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.WorkModel
import com.example.studentass.services.WorkApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_task_description.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TaskFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private val workService = WorkApiService.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task_type.text = when (TaskListFragment.currentTask.type) {
            "LAB" -> "Лабораторная работа"
            "COURSEWORK" -> "Курсовая работа"
            else -> "Практическая работа"
        }
        task_type.background = ContextCompat.getDrawable(context!!, when(TaskListFragment.currentTask.type){
            "LAB" -> R.drawable.laboratory_task_band
            "COURSEWORK" -> R.drawable.courework_task_band
            else -> R.drawable.practice_task_band
        })

        task_type.setTextColor(ContextCompat.getColor(context!!, when(TaskListFragment.currentTask.type){
            "LAB" -> R.color.colorTypeLaboratory
            "COURSEWORK" -> R.color.colorTypeCoursework
            else -> R.color.colorTypePractice
        }))

        teacherCommentCL.background = ContextCompat.getDrawable(context!!, R.drawable.teacher_comment_background_selector)

        task_descriptionCL.background = ContextCompat.getDrawable(context!!, R.drawable.task_decryption_back)

        task_name.text = TaskListFragment.currentTask.title
        task_description.text = TaskListFragment.currentTask.description

        task_name.setTextColor(ContextCompat.getColor(context!!, R.color.textTaskColor))
        task_description.setTextColor(ContextCompat.getColor(context!!, R.color.textTaskColor))


        loadWork()

        teacherCommentCL.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchUp(ChatFragment::class.java)
        }

        backCL.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_description, container, false)
    }

    private fun loadWork() {
        val header = "Bearer " + LoginFragment.token
        val userId = AccountFragment.currentUser.id
        val taskId = TaskListFragment.currentTask.id

        val jsonObject = JSONObject()
        jsonObject.put("key", "userId")
        jsonObject.put("operation", "==")
        jsonObject.put("value", userId)

        val jsonObject1 = JSONObject()
        jsonObject1.put("key", "taskId")
        jsonObject1.put("operation", "==")
        jsonObject1.put("value", taskId)

        val jsonObjectString = "[$jsonObject , $jsonObject1]"


        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())


        val disposableSubjectListRx = workService
            .getWorkByCriteria(header, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetWork(r) },
                { e ->
                    Toast.makeText(context, "Get work error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun onGetWork(r: List<WorkModel>) {
        if (r.isNotEmpty()) {

            mark_label.visibility = View.VISIBLE
            //teacherComment.visibility = View.VISIBLE
            teacherComment_label.visibility = View.VISIBLE

            val commentToWorks = "Комметарии к ответам (${r.size})"
            teacherComment_label.text = commentToWorks
            mark_label.text = when (r[0].mark) {
                "TREE" -> "Удовлетворительно"
                "FOUR" -> "Хорошо"
                "FIVE" -> "Отлично"
                else -> "Не удовлетворительно"
            }
            mark_label.setTextColor(ContextCompat.getColor(context!!, when(r[0].mark){
                "TREE" -> R.color.three
                "FOUR" -> R.color.four
                "FIVE" -> R.color.five
                else -> R.color.white
            }))
            mark_label.background = ContextCompat.getDrawable(context!!, when (r[0].mark) {
                "TREE" -> R.drawable.label_for_3
                "FOUR" -> R.drawable.ic_label_for_4
                "FIVE" -> R.drawable.ic_label_for_5
                else -> R.color.white
            })

            doneTaskIV.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_done_task
                )
            )
            doneLabel.text = "Сдано"
            //teacherComment.text = r[0].teacherComment
            //teacherComment.setTextColor(ContextCompat.getColor(context!!, R.color.textTaskColor))
        } else {
            doneTaskIV.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_not_done_task
                )
            )
            doneLabel.text = "Не сдано"
            mark_label.visibility = View.INVISIBLE
            //teacherComment.visibility = View.INVISIBLE
            teacherComment_label.visibility = View.INVISIBLE
            teacherCommentCL.visibility = View.INVISIBLE
        }
    }

}