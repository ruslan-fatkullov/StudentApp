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
import com.example.studentass.adapters.SchedulePairsRvItem
import com.example.studentass.models.Schedule
import com.example.studentass.models.ScheduleDay
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_subjects.*
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
    private var weekNum: Int? = null
    private var dayNum: Int? = null


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
        dayNum = 6
        daysIn[dayNum!!].requestFocus()

        /*var defaultItemFocusId = 3
        var days = ArrayList<ScheduleDaysLayoutItem>()
        days.add(ScheduleDaysLayoutItem("ПН", "0"))
        days.add(ScheduleDaysLayoutItem("ВТ", "0"))
        days.add(ScheduleDaysLayoutItem("СР", "0"))
        days.add(ScheduleDaysLayoutItem("ЧТ", "0"))
        days.add(ScheduleDaysLayoutItem("ПТ", "0"))
        days.add(ScheduleDaysLayoutItem("СБ", "0"))
        days.add(ScheduleDaysLayoutItem("ВС", "0"))*/
        /*scheduleDaysRv.hasFixedSize()
        scheduleDaysRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        scheduleDaysRv.adapter = ScheduleDaysLayoutAdapter(context!!, days)
        scheduleDaysRv.viewTreeObserver.addOnGlobalLayoutListener {
            val view = scheduleDaysRv.getChildAt(defaultItemFocusId)
            val viewHolder = scheduleDaysRv.findContainingViewHolder(view)
            viewHolder?.itemView?.requestFocus()
        }*/

        weekNum = 1
        weekTv.text = "Неделя $weekNum"

        schedulePairsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        //setPairsList(0, 0)


        // Получение расписания из сервиса
        thread {
            var text : String
            try {
                val scheduleJsonString = AuthActivity.sendGet("https://my-json-server.typicode.com/AntonScript/schedule-service/GroupStudent")
                val scheduleObject = GsonBuilder().create().fromJson(scheduleJsonString, Schedule::class.java)
                schedule = scheduleObject
                text = GsonBuilder().create().toJson(scheduleObject)
            } catch (e : Exception) {
                //Toast.makeText(context, "Schedule init error: $e", Toast.LENGTH_LONG).show()
                text = e.toString()
            }
            AuthActivity.mHandler.post {
                setPairsList(1, 2)
            }
        }
    }

    fun OnDayFocus(dayOfWeek: Int){
        Toast.makeText(context, "Day: $dayOfWeek", Toast.LENGTH_SHORT).show()
    }

    fun switchWeek() {
        if (weekNum == 1) weekNum = 2
        else weekNum = 1
        var text = "Неделя $weekNum"
        weekTv.text = text
    }

    fun setPairsList(week: Int, day: Int){
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
        if (scheduleDay == null && scheduleDay!!.coupels.size == 0) {
            schedulePairsRv.adapter = null
            return
        }
        var pairs = ArrayList<SchedulePairsRvItem>()
        schedulePairsRv.adapter = SchedulePairsRvAdapter(context!!, scheduleDay!!.coupels)
    }

    fun onPreviousWeekBnClick(view: View){
        switchWeek()

    }
    fun onNextWeekBnClick(view: View){
        switchWeek()

    }
}