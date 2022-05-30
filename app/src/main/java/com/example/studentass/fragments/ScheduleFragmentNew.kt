package com.example.studentass.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.R
import com.example.studentass.adapters.SchedulePairsRvAdapterNew
import com.example.studentass.fragments.MainFragment.Companion.colorTheme
import com.example.studentass.getAppCompatActivity
import com.example.studentass.models.Group
import com.example.studentass.models.scheduleNew.SubjectList
import com.example.studentass.models.scheduleNew.Timetable
import com.example.studentass.models.scheduleNew.TimetableLesson
import com.example.studentass.models.User
import com.example.studentass.services.GroupApiService
import com.example.studentass.services.ScheduleApiServiceNew
import com.example.studentass.services.UserApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import java.util.*


/*
 * Фрагмент расписания
 */
class ScheduleFragmentNew : Fragment() {
    private val scheduleApiServiceNew = ScheduleApiServiceNew.create()
    private var schedule: Timetable? = null
    private var weekNum: Int = 0
    private var dayNum: Int = 0
    private var daysIn: List<View>? = null
    private val calendar = Calendar.getInstance()
    private val compositeDisposable = CompositeDisposable()

    ////
    private var currentGroup = ""


    /*
     * Наполнение фрагмента элементами интерфейса
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }


    /*
     * Инициализация элементов интерфейса
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        scheduleLayout.background = colorTheme

        schedulePairsRv.visibility = View.INVISIBLE
        loadingImage.visibility = View.VISIBLE

        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.rot)
        loadingImage.startAnimation(animation)


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
            daysIn!![x].viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, _ ->
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
        schedulePairsRv.adapter = SchedulePairsRvAdapterNew(context!!)

        val scheduleGroupTvAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_dropdown_item_1line
        )
        scheduleGroupTv.setAdapter(scheduleGroupTvAdapter)

        val disposableGroupListRx = scheduleApiServiceNew
            .getGroupList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> onGetGroupList(r) },
                { e ->
                    Toast.makeText(context, "Get group list error: $e", Toast.LENGTH_LONG).show()
                }
            )
        compositeDisposable.add(disposableGroupListRx)

        updateScheduleBn.setOnClickListener {
            daysIn!![dayNum].requestFocus()
            val groupName = scheduleGroupTv.text.toString()
            val disposableGroupScheduleRx = scheduleApiServiceNew
                .getGroupSchedule(groupName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { r -> onGetGroupSchedule(r) },
                    { e ->
                        Toast.makeText(
                            context,
                            "Get group schedule error: $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            compositeDisposable.add(disposableGroupScheduleRx)
        }

        getUser()


        val adapter = schedulePairsRv.adapter as SchedulePairsRvAdapterNew
        adapter.setOnItemClickListener(object : SchedulePairsRvAdapterNew.OnItemClickListener {
            override fun setOnClickListener(position: Int) {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            }

        })
        adapter.notifyDataSetChanged()


        onHiddenChanged(false)
    }


    /*
     * Управление заголовком страницы и обновлением данных о дате
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            getAppCompatActivity<MainActivity>()?.actionBar?.title = "Расписание"
            daysIn!![dayNum].requestFocus()
            updateWeek()
            updateDaysOfMonth()
        }
    }

    /*
     * Очистка мусора
     */
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


    /*
     * Вызывается при успешном получении списка групп
     */
    private fun onGetGroupList(groupList: SubjectList?) {
        if (groupList != null) {
            @Suppress("UNCHECKED_CAST")
            val adapter = scheduleGroupTv.adapter as ArrayAdapter<String>
            adapter.addAll(groupList.response.toList())
        }
    }


    /*
     * Вызывается при успешном получении списка пар
     */
    private fun onGetGroupSchedule(schedule: Timetable) {

        loadingImage.clearAnimation()
        loadingImage.visibility = View.INVISIBLE
        schedulePairsRv.visibility = View.VISIBLE

        this.schedule = schedule
        scheduleGroupTv.setText(currentGroup)
        updatePairsList()
    }


    /*
     * Переводит день недели из американского формата в европейский
     */
    private fun formatDayOfWeek(dayOfWeek: Int): Int {
        var newDayOfWeek = dayOfWeek - 2
        if (newDayOfWeek < 0) newDayOfWeek = 6
        return newDayOfWeek
    }


    /*
     * Обновление списка пар
     */
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
        val adapter = schedulePairsRv.adapter as SchedulePairsRvAdapterNew
        if (day == 7) {
            adapter.dataList.clear()
            adapter.pairNumberArray.clear()
            pairsAbsenceTv.visibility = View.VISIBLE
        } else {
            val scheduleWeek = schedule?.response?.weeks?.get(week - 1)
            val scheduleDay = scheduleWeek?.days?.get(day - 1)

            var isEmptyDay = true
            if (scheduleDay != null) {
                for (i in scheduleDay.lessons) {
                    if (i.isNotEmpty()) {
                        isEmptyDay = false
                    }
                }
            }

            if (scheduleDay == null || isEmptyDay) {
                adapter.dataList.clear()
                adapter.pairNumberArray.clear()
                pairsAbsenceTv.visibility = View.VISIBLE
            } else {
                adapter.dataList.clear()
                adapter.pairNumberArray.clear()
                for (i in 0 until scheduleDay.lessons.size) {
                    if (scheduleDay.lessons[i].isNotEmpty()) {
                        adapter.pairNumber = i + 1
                        adapter.pairNumberArray.add(adapter.pairNumber)
                        adapter.dataList.add(scheduleDay.lessons[i] as ArrayList<TimetableLesson>)
                    }
                }
                adapter.dataYear = calendar.get(Calendar.YEAR)
                adapter.dataDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                pairsAbsenceTv.visibility = View.INVISIBLE
            }
        }

        adapter.notifyDataSetChanged()
    }


    /*
     * Обновление надписи с номером недели
     */
    private fun updateWeek() {
        weekTv.text =
            if (weekNum == 0) getText(R.string.schedule_first_week) else getText(R.string.schedule_second_week)
    }


    /*
     * Обновление чисел месяца
     */
    private fun updateDaysOfMonth() {
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.add(Calendar.DAY_OF_WEEK, -dayNum)
        for (i in 0..6) {
            daysIn!![i].dayTextView.text = tempCalendar.get(Calendar.DAY_OF_MONTH).toString()
            tempCalendar.add(Calendar.DAY_OF_WEEK, 1)
        }
    }


    /*
     * Обновление надписи с месяцем и годом
     */
    private fun updateMonthAndYear() {
        val text = "${
            getString(
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
            )
        } ${calendar.get(Calendar.YEAR)}"
        dateTv.text = text
    }


    /*
     * Обработчик нажатия на кнопки переключения недели. dir - указывает направление, в котором нужно пролистать неделю
     */
    private fun onWeekBnClick(dir: Int) {
        weekNum = if (weekNum == 0) 1 else 0
        calendar.add(Calendar.DAY_OF_WEEK, 7 * dir)
        updateWeek()
        updateMonthAndYear()
        updateDaysOfMonth()

        if (schedule != null) {
            try {
                updatePairsList()
            } catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /*
     * Обработчик нажатия на кнопки дней недели
     */
    private fun onDayFocus(dayOfWeek: Int) {
        calendar.add(Calendar.DAY_OF_WEEK, -dayNum)
        dayNum = dayOfWeek
        calendar.add(Calendar.DAY_OF_WEEK, dayNum)
        updateMonthAndYear()

        if (schedule != null) {
            try {
                updatePairsList()
            } catch (e: Exception) {
                Toast.makeText(context, "Schedule update error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUser() {
        val userApiService = UserApiService.create()
        val compositeDisposable = CompositeDisposable()
        val requestBody = "Bearer " + LoginFragment.token
        val disposableSubjectListRx = userApiService
            .getUser(requestBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { r -> getStudyGroup(r, requestBody) },
                { e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show() }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun getStudyGroup(r: User, requestBody: String) {
        val groupApiService = GroupApiService.create()
        val disposableSubjectListRx = groupApiService
            .getGroupById(requestBody, r.studyGroupId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { group -> getSch(group) },
                { e -> Toast.makeText(context, "Get user error: $e", Toast.LENGTH_LONG).show() }
            )
        compositeDisposable.add(disposableSubjectListRx)
    }

    private fun getSch(r: Group) {
        currentGroup = r.shortName
        val disposableGroupScheduleRx = scheduleApiServiceNew
            .getGroupSchedule(r.shortName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { timeTable -> onGetGroupSchedule(timeTable) },
                { e ->
                    Toast.makeText(context, "Get group schedule error: $e", Toast.LENGTH_LONG)
                        .show()
                }
            )
        compositeDisposable.add(disposableGroupScheduleRx)

    }
}