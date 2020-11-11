package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.AuthActivity
import com.example.studentass.R
import com.example.studentass.adapters.SchedulePairsRvAdapter
import com.example.studentass.models.Schedule
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment() {
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScheduleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var schedule: Schedule? = null
    private var weekNum: Int = 0
    private var dayNum: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousWeekBn.setOnClickListener { onPreviousWeekBnClick(view) }
        nextWeekBn.setOnClickListener { onNextWeekBnClick(view) }

        dayNum = 0
        weekNum = 0
        dayIn1.dayOfWeekTextView.text = "ПН"
        dayIn2.dayOfWeekTextView.text = "ВТ"
        dayIn3.dayOfWeekTextView.text = "СР"
        dayIn4.dayOfWeekTextView.text = "ЧТ"
        dayIn5.dayOfWeekTextView.text = "ПТ"
        dayIn6.dayOfWeekTextView.text = "СБ"
        dayIn7.dayOfWeekTextView.text = "ВС"
        val daysIn = listOf<View>(dayIn1, dayIn2, dayIn3, dayIn4, dayIn5, dayIn6, dayIn7)
        for (x in 0..6) {
            daysIn[x].setOnFocusChangeListener {_, _ -> OnDayFocus(x)}
        }
        daysIn[dayNum].requestFocus()
        weekTv.text = "Неделя ${weekNum + 1}"

        schedulePairsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        //setPairsList(0, 0)


        // Получение расписания из сервиса
        thread {
            try {
                val scheduleJsonString = AuthActivity.sendGet("https://my-json-server.typicode.com/AntonScript/schedule-service/GroupStudent")
                val scheduleObject = GsonBuilder().create().fromJson(scheduleJsonString, Schedule::class.java)
                schedule = scheduleObject
            } catch (e : Exception) {
                Toast.makeText(context, "Schedule init error: $e", Toast.LENGTH_LONG).show()
            }
            AuthActivity.mHandler.post {
                try {
                    updatePairsList()
                }
                catch (e: Exception) {
                    Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun OnDayFocus(dayOfWeek: Int){
        dayNum = dayOfWeek

        if (schedule != null) {
            try {
                updatePairsList()
            }
            catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun switchWeek() {
        if (weekNum == 0) weekNum = 1
        else weekNum = 0
        weekTv.text = "Неделя ${weekNum + 1}"

        if (schedule != null) {
            try {
                updatePairsList()
            }
            catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePairsList() {
        val day = dayNum + 1
        val week = weekNum + 1

        if (week !in 1..2) {
            throw Exception("Invalid week index")
        }
        if (day !in 1..7) {
            throw Exception("Invalid day index")
        }
        if (schedule == null) {
            throw Exception("Schedule is null")
        }

        val scheduleDay = schedule?.days?.firstOrNull { d -> d.number_day == day && d.numberWeek == week }
        if (scheduleDay == null || scheduleDay.coupels.isEmpty()) {
            schedulePairsRv.adapter = null
            return
        }

        schedulePairsRv.adapter = SchedulePairsRvAdapter(context!!, scheduleDay.coupels)
    }

    private fun onPreviousWeekBnClick(view: View){
        switchWeek()

    }
    private fun onNextWeekBnClick(view: View){
        switchWeek()

    }
}