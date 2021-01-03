package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SchedulePairsRvAdapter
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Schedule
import com.example.studentass.models.ScheduleDayCouple
import com.example.studentass.services.ScheduleApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import java.util.*


class ScheduleFragment : Fragment() {
    private val scheduleApiService = ScheduleApiService.create()
    private var schedule: Schedule? = null
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

    private var compositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousWeekBn.setOnClickListener { run { onWeekBnClick(-1) } }
        nextWeekBn.setOnClickListener { run { onWeekBnClick(1) } }

        daysIn = listOf<View>(dayIn1, dayIn2, dayIn3, dayIn4, dayIn5, dayIn6, dayIn7)
        dayNum = formatDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        weekNum = 0
        dayIn1.dayOfWeekTextView.text = getString(R.string.schedule_monday)
        dayIn2.dayOfWeekTextView.text = getString(R.string.schedule_tuesday)
        dayIn3.dayOfWeekTextView.text = getString(R.string.schedule_wednesday)
        dayIn4.dayOfWeekTextView.text = getString(R.string.schedule_thursday)
        dayIn5.dayOfWeekTextView.text = getString(R.string.schedule_friday)
        dayIn6.dayOfWeekTextView.text = getString(R.string.schedule_saturday)
        dayIn7.dayOfWeekTextView.text = getString(R.string.schedule_sunday)
        for (x in 0..6) {
            daysIn!![x].setOnFocusChangeListener { newFocus, _ ->
                newFocus?.dayOfWeekTextView?.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorScheduleDayOfWeekOnFocus
                    )
                )
                newFocus?.dayTextView?.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorScheduleDayOnFocus
                    )
                )
                onDayFocus(x)
            }
            daysIn!![x].viewTreeObserver.addOnGlobalFocusChangeListener{ oldFocus, _ ->
                oldFocus?.dayOfWeekTextView?.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorScheduleDayOfWeekDefault
                    )
                )
                oldFocus?.dayTextView?.setTextColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorScheduleDayDefault
                    )
                )
            }
        }

        schedulePairsRv.layoutManager = LinearLayoutManager(
            context!!,
            LinearLayoutManager.VERTICAL,
            false
        )
        schedulePairsRv.adapter = SchedulePairsRvAdapter(context!!)

        val scheduleGroupTvAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line
        )
        scheduleGroupTv.setAdapter(scheduleGroupTvAdapter)

        val disposableGroupListRx = scheduleApiService
            .getGroupList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe (
                {r -> onGetGroupList(r)},
                {e -> Toast.makeText(context, "Get group list error: $e", Toast.LENGTH_LONG).show()}
            )
        compositeDisposable.add(disposableGroupListRx)

        updateScheduleBn.setOnClickListener {
            daysIn!![dayNum].requestFocus()
            val groupName = scheduleGroupTv.text.toString()
            val disposableGroupScheduleRx = scheduleApiService
                .getGroupSchedule(groupName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe (
                    {r -> onGetGroupSchedule(r)},
                    {e -> Toast.makeText(context, "Get group schedule error: $e", Toast.LENGTH_LONG).show()}
                )
            compositeDisposable.add(disposableGroupScheduleRx)
        }

        onHiddenChanged(false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Расписание"
            daysIn!![dayNum].requestFocus()
            updateWeek()
            updateDaysOfMonth()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun onGetGroupList(groupList: Array<String>?) {
        if (groupList != null) {
            @Suppress("UNCHECKED_CAST")
            val adapter = scheduleGroupTv.adapter as ArrayAdapter<String>
            adapter.addAll(groupList.toList())
        }
    }

    private fun onGetGroupSchedule(schedule: Schedule) {
        this.schedule = schedule
        updatePairsList()
    }

    private fun formatDayOfWeek(dayOfWeek: Int): Int {
        var newDayOfWeek = dayOfWeek - 2
        if (newDayOfWeek < 0) newDayOfWeek = 6
        return newDayOfWeek
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
        weekTv.text = if (weekNum == 0) getText(R.string.schedule_first_week) else getText(R.string.schedule_second_week)
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
        val text = "${getString(
            when (calendar.get(Calendar.MONTH)) {
                Calendar.JANUARY -> R.string.schedule_january
                Calendar.FEBRUARY -> R.string.schedule_february
                Calendar.MARCH -> R.string.schedule_march
                Calendar.APRIL -> R.string.schedule_april
                Calendar.MAY -> R.string.schedule_may
                Calendar.JUNE -> R.string.schedule_june
                Calendar.JULY -> R.string.schedule_july
                Calendar.AUGUST -> R.string.schedule_august
                Calendar.SEPTEMBER -> R.string.schedule_september
                Calendar.OCTOBER -> R.string.schedule_october
                Calendar.NOVEMBER -> R.string.schedule_november
                Calendar.DECEMBER -> R.string.schedule_december
                else -> R.string.error
            }
        )} ${calendar.get(Calendar.YEAR)}"
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