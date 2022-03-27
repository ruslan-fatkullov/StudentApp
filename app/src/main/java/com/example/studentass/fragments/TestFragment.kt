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
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.TestAnswer
import com.example.studentass.models.TestQuestion
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : Fragment() {
    private lateinit var sfm: FragmentManager

    //private val compositeDisposable = CompositeDisposable()

    companion object{
        var currentQuestion : TestQuestion? = null
        var currentQuestionId : Int = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = getFakeData()

        var qFragment : Fragment?

        countOfQuestions.text = data.size.toString()

        currentOfQuestions.text = (currentQuestionId + 1).toString()


        currentQuestion = data[currentQuestionId]
        qFragment = when(currentQuestion!!.questionType){
            "SELECT" -> QuestionTypeSelectFragment()
            "MATCH" -> QuestionTypeMatchFragment()
            else -> QuestionTypeWriteFragment()
        }
        sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
        sfm.beginTransaction().add(questionFrame.id, qFragment).commit()


        previousQuestionBtn.setOnClickListener {
            if (currentQuestionId > 0){
                nextQuestionBtn.text = "Далее"
                nextQuestionBtn.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                sfm.beginTransaction().remove(qFragment!!).commit()
                currentQuestion = data[--currentQuestionId]
                qFragment = when(currentQuestion!!.questionType){
                    "SELECT" -> QuestionTypeSelectFragment()
                    "MATCH" -> QuestionTypeMatchFragment()
                    else -> QuestionTypeWriteFragment()
                }
                sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
                sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
                currentOfQuestions.text = (currentQuestionId + 1).toString()
            }
        }
        nextQuestionBtn.setOnClickListener {
            //добавить последние ответы
            if(currentQuestionId < data.size - 1){
                ///добавить другие типы
                showUserAnswer(context, QuestionTypeSelectFragment.questionId, QuestionTypeSelectFragment.answ)
                QuestionTypeSelectFragment.answ.clear()
                ///добавить другие типы

                sfm.beginTransaction().remove(qFragment!!).commit()
                currentQuestion = data[++currentQuestionId]
                qFragment = when(currentQuestion!!.questionType){
                    "SELECT" -> QuestionTypeSelectFragment()
                    "MATCH" -> QuestionTypeMatchFragment()
                    else -> QuestionTypeWriteFragment()
                }
                sfm = getAppCompatActivity<MainActivity>()!!.fragmentManager
                sfm.beginTransaction().add(questionFrame.id, qFragment!!).commit()
                if (currentQuestionId == data.size - 1){
                    nextQuestionBtn.setBackgroundColor(resources.getColor(R.color.colorGreen))
                    nextQuestionBtn.text = "Завершить"
                }
                currentOfQuestions.text = (currentQuestionId + 1).toString()
            }else{
                ///добавить другие типы
                showUserAnswer(context, QuestionTypeSelectFragment.questionId, QuestionTypeSelectFragment.answ)
                QuestionTypeSelectFragment.answ.clear()
                ///добавить другие типы

                currentQuestionId = 0
                currentQuestion = null
                sfm.beginTransaction().remove(qFragment!!).commit()
                getAppCompatActivity<MainActivity>()?.switchDown()
                Toast.makeText(context, "тест завершен", Toast.LENGTH_SHORT).show()
            }
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
//        if (!hidden) {
//            getAppCompatActivity<MainActivity>()?.actionBar?.hide()
//        }
    }
}

private fun getFakeData(): ArrayList<TestQuestion> {
    val array1 = arrayListOf<Long>()
    val answer1 = arrayListOf<TestAnswer>()
    answer1.add(TestAnswer(36, "логика", null))
    answer1.add(TestAnswer(35, "начало", null))
    answer1.add(TestAnswer(37, "конец", null))
    answer1.add(TestAnswer(38, "логика", null))
    answer1.add(TestAnswer(39, "начало", null))
    answer1.add(TestAnswer(40, "конец", null))
    answer1.add(TestAnswer(3, "логика", null))
    answer1.add(TestAnswer(5, "начало", null))
    answer1.add(TestAnswer(7, "конец", null))
    val array2 = arrayListOf<Long>()
    val answer2 = arrayListOf<TestAnswer>()
    answer2.add(TestAnswer(36, "логика", null))
    answer2.add(TestAnswer(35, "начало", null))
    answer2.add(TestAnswer(37, "конец", null))

    val array3 = arrayListOf<Long>()
    val answer3 = arrayListOf<TestAnswer>()

    val quest = arrayListOf<TestQuestion>()
    quest.add(TestQuestion(14, "Что помечается овалом?","SELECT", 1, array1, answer1))
    quest.add(TestQuestion(4, "Поsdfdsfаммы","WRITE", 1, array1, answer1))
    quest.add(TestQuestion(1, "Посsdfdsf","SELECT", 1, array1, answer1))
    quest.add(TestQuestion(47, "Послеsdfsdfограммы","SELECT", 1, array1, answer1))
//    quest.add(TestQuestion(5, "Автомат, у которого состояния проставляются на операторных вершинах?","WRITE", 3, array1, answer1))
//    quest.add(TestQuestion(12, "ДА? ","MATCH", 2, array3, answer3))
//    quest.add(TestQuestion(12, "Нет? ","MATCH", 2, array3, answer3))
//    quest.add(TestQuestion(12, "Жопа? ","MATCH", 2, array3, answer3))
//    quest.add(TestQuestion(12, "Может быть? ","MATCH", 2, array3, answer3))

    return quest
}

private fun showUserAnswer(context: Context?, questionId: Long?, answ: ArrayList<Long>) {
    Toast.makeText(context, questionId.toString(), Toast.LENGTH_SHORT).show()
    for (i in answ){
        Toast.makeText(context, i.toString(), Toast.LENGTH_SHORT).show()
    }

}