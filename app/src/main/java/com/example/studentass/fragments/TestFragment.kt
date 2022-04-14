package com.example.studentass.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.fragments.QuestionTypeWriteFragment.Companion.anWindow
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.TestQuestion
import com.example.studentass.models.testResultModel.testResult
import com.example.studentass.services.SubjectApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class TestFragment : Fragment() {
    private val subjectApiService = SubjectApiService.create()
    private lateinit var sfm: FragmentManager
    private var testQuestions: ArrayList<TestQuestion>? = null
    private var qFragment : Fragment? = null
    private var countOfquest : Int? = null


    companion object{
        var currentQuestion : TestQuestion? = null
        var currentQuestionId : Int = 0
        var ratingOfTest: Long? = null
        var requestToCheckTest: String? = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAppCompatActivity<MainActivity>()?.actionBar?.hide()


        loadTest(10)

        currentOfQuestions.text = (currentQuestionId + 1).toString()

        testTheme.text = TestListFragment.currentTest?.theme?.name


        nextQuestCheck.visibility = View.INVISIBLE
        nextQuestArrow.visibility = View.VISIBLE

//        var mainDraw = DrawableCompat.wrap(context?.let { ContextCompat.getDrawable(it, R.drawable.select_answer_item_background) }!!)
//        DrawableCompat.setTintMode(mainDraw, PorterDuff.Mode.SRC_ATOP);
//        var close = DrawableCompat.wrap(context?.let { ContextCompat.getDrawable(it, R.drawable.select_answer_item_background_cliked) }!!)
//        DrawableCompat.setTintMode(close, PorterDuff.Mode.SRC_ATOP);
//        previousQuestionBtn.background = mainDraw
//        nextQuestionBtn.background = mainDraw
        //questionTitle.setBackgroundColor(ContextCompat.getColor(context!!, R.color.testGreenColor))

//        previousQuestionBtn.setOnClickListener {
//
//
//
//            if (currentQuestionId > 0){
//                nextQuestionBtn.text = "Далее"
//                nextQuestionBtn.background = mainDraw
//                sfm.beginTransaction().remove(qFragment!!).commit()
//                currentQuestion = testQuestions?.get(--currentQuestionId)
//                updateProgressBar(currentQuestionId + 1)
//                qFragment = when(currentQuestion!!.questionType){
//                    "SELECT" -> QuestionTypeSelectFragment()
//                    "MATCH" -> QuestionTypeMatchFragment()
//                    "SEQUENCE" -> QuestionTypeSequenceFragment()
//                    else -> QuestionTypeWriteFragment()
//                }
//                sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
//                sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
//                currentOfQuestions.text = (currentQuestionId + 1).toString()
//            }
//        }
//        nextQuestionBtn.setOnClickListener {
//            //добавляем ответы
//
//            loadAnswers()
//            ///
//            if(currentQuestionId < testQuestions?.size!! - 1){
//
//                sfm.beginTransaction().remove(qFragment!!).commit()
//
//                //currentQuestion = data[++currentQuestionId]
//                currentQuestion = testQuestions?.get(++currentQuestionId)
//                updateProgressBar(currentQuestionId + 1)
//                qFragment = when(currentQuestion!!.questionType){
//                    "SELECT" -> QuestionTypeSelectFragment()
//                    "MATCH" -> QuestionTypeMatchFragment()
//                    "SEQUENCE" -> QuestionTypeSequenceFragment()
//                    else -> QuestionTypeWriteFragment()
//                }
//                sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
//                sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
//                //if (currentQuestionId == data.size - 1){
//                if (currentQuestionId == testQuestions?.size?.minus(1)){
//                    nextQuestionBtn.background = close
//                    nextQuestionBtn.text = "Завершить"
//                }
//                currentOfQuestions.text = (currentQuestionId + 1).toString()
//            }else{
//
//                currentQuestionId = 0
//                currentQuestion = null
//                checkTest()
//
//            }
//        }
        nextQuestBut.setOnClickListener {
            loadAnswers()
            ///
            if(currentQuestionId < testQuestions?.size!! - 1){

                sfm.beginTransaction().remove(qFragment!!).commit()

                //currentQuestion = data[++currentQuestionId]
                currentQuestion = testQuestions?.get(++currentQuestionId)
                updateProgressBar(currentQuestionId + 1)
                qFragment = when(currentQuestion!!.questionType){
                    "SELECT" -> QuestionTypeSelectFragment()
                    "MATCH" -> QuestionTypeMatchFragment()
                    "SEQUENCE" -> QuestionTypeSequenceFragment()
                    else -> QuestionTypeWriteFragment()
                }
                sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
                sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
                //if (currentQuestionId == data.size - 1){
                if (currentQuestionId == testQuestions?.size?.minus(1)){
                    nextQuestArrow.visibility = View.INVISIBLE
                    //nextQuestBut.text = "Завершить"
                    nextQuestCheck.visibility = View.VISIBLE
                }
                currentOfQuestions.text = (currentQuestionId + 1).toString()
            }else{

                currentQuestionId = 0
                currentQuestion = null
                checkTest()

            }
        }
        closeTab.setOnClickListener {
            getAppCompatActivity<MainActivity>()?.switchDown()
        }



        super.onHiddenChanged(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        getAppCompatActivity<MainActivity>()?.actionBar?.hide()
    }

    override fun onDestroyView() {
        currentQuestionId = 0
        currentQuestion = null
        requestToCheckTest = ""
        super.onDestroyView()
    }

    private fun onCheckTest(context: Context?, t: testResult) {
        ratingOfTest = t.rating
        sfm.beginTransaction().remove(qFragment!!).commit()
        getAppCompatActivity<MainActivity>()?.switchSideways(TestResultFragment::class.java)
    }

    private fun loadAnswers() {
        when(currentQuestion?.questionType){
            "SELECT" -> {
                val asd = "{${'"'}questionId${'"'}:${QuestionTypeSelectFragment.questionId},${'"'}answers${'"'}:${QuestionTypeSelectFragment.answ}}"
                requestToCheckTest += "${asd},"
            }
            "WRITE" -> {
                val aw = anWindow?.text
                val asd = "{${'"'}questionId${'"'}:${QuestionTypeWriteFragment.questionId},${'"'}answers${'"'}:[${'"'}${aw}${'"'}]}"
                requestToCheckTest += "${asd},"
            }
            "SEQUENCE" -> {
                val asd = "{${'"'}questionId${'"'}:${QuestionTypeSequenceFragment.questionId},${'"'}answers${'"'}:${QuestionTypeSequenceFragment.answ}}"
                requestToCheckTest += "${asd},"
            }
            else -> {
                //MATCH VARIANT
            }
        }
    }


    private fun getTest(questions: List<TestQuestion>){


        testQuestions = questions as ArrayList<TestQuestion>
        var i = 0
        while (i < testQuestions!!.size){
            if (testQuestions!![i].questionType == "MATCH"){
                testQuestions!!.removeAt(i)
            }else{
                i++
            }
        }

        countOfquest = testQuestions?.size
        countOfQuestions.text = countOfquest.toString()
        progressBarTest.max = countOfquest!!

        updateProgressBar(currentQuestionId + 1)

        currentQuestion = testQuestions?.get(currentQuestionId)


        when(currentQuestion!!.questionType){
            "SELECT" -> qFragment = QuestionTypeSelectFragment()
            "WRITE" -> qFragment = QuestionTypeWriteFragment()
            "SEQUENCE" -> qFragment = QuestionTypeSequenceFragment()
            else -> qFragment = QuestionTypeMatchFragment()
        }
        sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
        sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
    }

    private fun loadTest(limit: Long){
        val auth = "Bearer " + LoginFragment.token
        val themeId: Long? = TestListFragment.currentTest?.theme?.id
        val disposableSubjectListRx = subjectApiService
            .getTestByThemeId(auth, themeId, limit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> getTest(r)},
                {e -> Toast.makeText(context, "Get question list error: $e", Toast.LENGTH_LONG).show()}
            )
    }

    private fun checkTest(){
        val subApi = SubjectApiService.create()

        val tok = "Bearer " + LoginFragment.token
        val dfs = requestToCheckTest?.dropLast(1)
        val stringRequest = "[${dfs}]"
        val requestBody = stringRequest.toRequestBody("application/json".toMediaTypeOrNull())
        val res = subApi
            .checkTest(tok, requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> onCheckTest(context, r)},
                {e -> Toast.makeText(context, "Get test result error: $e", Toast.LENGTH_LONG).show()}
            )
    }

    fun updateProgressBar(value: Int){
        progressBarTest.progress = value
    }

}