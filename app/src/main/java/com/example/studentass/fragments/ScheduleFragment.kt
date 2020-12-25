package com.example.studentass.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.MainActivity.Companion.mainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SchedulePairsRvAdapter
import com.example.studentass.models.Schedule
import com.example.studentass.models.ScheduleDayCouple
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class ScheduleFragment : Fragment() {
    private var schedule: Schedule? = null
    private var groupList: List<String>? = null
    private var weekNum: Int = 0
    private var dayNum: Int = 0
    private var daysIn: List<View>? = null
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousWeekBn.setOnClickListener { run { onWeekBnClick(-1) } }
        nextWeekBn.setOnClickListener { run { onWeekBnClick(1) } }

        daysIn = listOf<View>(dayIn1, dayIn2, dayIn3, dayIn4, dayIn5, dayIn6, dayIn7)
        dayNum = formatDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        weekNum = 0
        dayIn1.dayOfWeekTextView.text = "ПН"
        dayIn2.dayOfWeekTextView.text = "ВТ"
        dayIn3.dayOfWeekTextView.text = "СР"
        dayIn4.dayOfWeekTextView.text = "ЧТ"
        dayIn5.dayOfWeekTextView.text = "ПТ"
        dayIn6.dayOfWeekTextView.text = "СБ"
        dayIn7.dayOfWeekTextView.text = "ВС"
        for (x in 0..6) {
            daysIn!![x].setOnFocusChangeListener { newFocus, _ ->
                newFocus?.dayOfWeekTextView?.setTextColor(ContextCompat.getColor(context!!, R.color.colorScheduleDayOfWeekOnFocus))
                newFocus?.dayTextView?.setTextColor(ContextCompat.getColor(context!!, R.color.colorScheduleDayOnFocus))
                onDayFocus(x)
            }
            daysIn!![x].viewTreeObserver.addOnGlobalFocusChangeListener{ oldFocus, _ ->
                oldFocus?.dayOfWeekTextView?.setTextColor(ContextCompat.getColor(context!!, R.color.colorScheduleDayOfWeekDefault))
                oldFocus?.dayTextView?.setTextColor(ContextCompat.getColor(context!!, R.color.colorScheduleDayDefault))
            }
        }

        schedulePairsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        schedulePairsRv.adapter = SchedulePairsRvAdapter(context!!)
        //setPairsList(0, 0)

        val scheduleGroupTvAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_dropdown_item_1line)
        scheduleGroupTv.setAdapter(scheduleGroupTvAdapter)
        scheduleGroupTv.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                daysIn!![dayNum].requestFocus()
                val groupName = scheduleGroupTv.text.toString()
                getSchedule(groupName)
            }
        }
        getGroupList()

        onHiddenChanged(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            mainActivity.actionBar.title = "Расписание"
            daysIn!![dayNum].requestFocus()
            updateWeek()
            updateDaysOfMonth()
        }
    }

    private fun getGroupList() {
        val url = "http://test.asus.russianitgroup.ru/api/schedule/group-list"
        val request = Request.Builder().url(url)
        thread {
            try {
                val response = LoginFragment.executeJwtRequest(request)
                try {
                    val groupsArray = GsonBuilder().create().fromJson(response.body!!.string(), Array<String>::class.java)
                    groupList = groupsArray.toList()
                } catch (e : Exception) {
                    MainActivity.mHandler.post {
                        Toast.makeText(context, "Group list interpretation error: $e", Toast.LENGTH_LONG).show()
                    }
                }
                MainActivity.mHandler.post {
                    try {
                        updateGroupList()
                    } catch (e : Exception) {
                        Toast.makeText(context, "Group list init error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Group list request error: $e", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getSchedule(groupName: String) {
        val url = "http://test.asus.russianitgroup.ru/api/schedule/group?nameGroup=$groupName"
        val request = Request.Builder().url(url)
        thread {
            try {
                val response = LoginFragment.executeJwtRequest(request)
                try {
                    val scheduleObject = GsonBuilder().create().fromJson(response.body!!.string(), Schedule::class.java)
                    schedule = scheduleObject
                } catch (e : Exception) {
                    MainActivity.mHandler.post {
                        Toast.makeText(context, "Schedule interpretation error: $e", Toast.LENGTH_LONG).show()
                    }
                }
                MainActivity.mHandler.post {
                    try {
                        updatePairsList()
                    } catch (e : Exception) {
                        Toast.makeText(context, "Schedule init error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Schedule request error: $e", Toast.LENGTH_LONG).show()
                }
            }
        }

        /*val url = "http://ef462968066b.ngrok.io/group?nameGroup=ИВТАПбд-31"
        //val url = "http://test.asus.russianitgroup.ru/api/schedule/group?nameGroup=ИВТАПбд-31"
        //val url = "https://my-json-server.typicode.com/AntonScript/schedule-service/GroupStudent"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                MainActivity.mHandler.post {
                    Toast.makeText(context, "Schedule request error: $e", Toast.LENGTH_LONG).show()
                }
            }
            override fun onResponse(call: Call, response: Response) {
                try {
                    val scheduleObject = GsonBuilder().create().fromJson(response.body!!.string(), Schedule::class.java)
                    schedule = scheduleObject
                } catch (e : Exception) {
                    Toast.makeText(context, "Schedule interpretation error: $e", Toast.LENGTH_LONG).show()
                }
                MainActivity.mHandler.post {
                    try {
                        updatePairsList()
                        pairsPb.visibility = View.GONE
                    } catch (e : Exception) {
                        Toast.makeText(context, "Schedule init error: $e", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })*/
    }

    private fun formatDayOfWeek(dayOfWeek: Int): Int {
        var newDayOfWeek = dayOfWeek - 2
        if (newDayOfWeek < 0) newDayOfWeek = 6
        return newDayOfWeek
    }

    private fun updateGroupList() {
        if (groupList != null) {
            val adapter = scheduleGroupTv.adapter as ArrayAdapter<String>
            adapter.addAll(groupList!!)
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

        val adapter = schedulePairsRv.adapter as SchedulePairsRvAdapter
        val scheduleDay = schedule?.days?.firstOrNull { d -> d.number_day == day && d.numberWeek == week }
        if (scheduleDay == null || scheduleDay.coupels.isEmpty()) {
            adapter.dataList.clear()
            pairsAbsenceTv.visibility = View.VISIBLE
        }
        else {
            adapter.dataList = scheduleDay.coupels.toMutableList() as ArrayList<ScheduleDayCouple>
            adapter.dataYear = calendar.get(Calendar.YEAR)
            adapter.dataDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
            pairsAbsenceTv.visibility = View.INVISIBLE
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateWeek() {
        val text = "${weekNum + 1} неделя"
        weekTv.text = text
    }

    private fun updateDaysOfMonth() {
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.add(Calendar.DAY_OF_WEEK, -dayNum)
        for (i in 0..6) {
            daysIn!![i].dayTextView.text = tempCalendar.get(Calendar.DAY_OF_MONTH).toString()
            tempCalendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }

    private fun updateMonthAndYear() {
        val text = "${when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> "Январь"
            Calendar.FEBRUARY -> "Февраль"
            Calendar.MARCH -> "Март"
            Calendar.APRIL -> "Апрель"
            Calendar.MAY -> "Май"
            Calendar.JUNE -> "Июнь"
            Calendar.JULY -> "Июль"
            Calendar.AUGUST -> "Август"
            Calendar.SEPTEMBER -> "Сентябрь"
            Calendar.OCTOBER -> "Октябрь"
            Calendar.NOVEMBER -> "Ноябрь"
            Calendar.DECEMBER -> "Декабрь"
            else -> "Error"
        }} ${calendar.get(Calendar.YEAR)}"
        dateTv.text = text
    }

    private fun onWeekBnClick(dir: Int){
        weekNum = if (weekNum == 0) 1 else 0
        calendar.add(Calendar.DAY_OF_WEEK, 7 * dir)
        updateWeek()
        updateMonthAndYear()
        updateDaysOfMonth()

        if (schedule != null) {
            try {
                updatePairsList()
            }
            catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onDayFocus(dayOfWeek: Int) {
        calendar.add(Calendar.DAY_OF_WEEK, -dayNum)
        dayNum = dayOfWeek
        calendar.add(Calendar.DAY_OF_WEEK, dayNum)
        updateMonthAndYear()

        if (schedule != null) {
            try {
                updatePairsList()
            }
            catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}