package com.example.studentass.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.MainActivity
import com.example.studentass.MainActivity2
import com.example.studentass.R
import com.example.studentass.adapters.ScheduleDaysLayoutAdapter
import com.example.studentass.adapters.ScheduleDaysLayoutItem
import com.example.studentass.models.Schedule
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_subjects.*
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        var defaultItemFocusId = 3
        var days = ArrayList<ScheduleDaysLayoutItem>()
        days.add(ScheduleDaysLayoutItem("ПН", "0"))
        days.add(ScheduleDaysLayoutItem("ВТ", "0"))
        days.add(ScheduleDaysLayoutItem("СР", "0"))
        days.add(ScheduleDaysLayoutItem("ЧТ", "0"))
        days.add(ScheduleDaysLayoutItem("ПТ", "0"))
        days.add(ScheduleDaysLayoutItem("СБ", "0"))
        days.add(ScheduleDaysLayoutItem("ВС", "0"))
        scheduleDaysLayout.hasFixedSize()
        scheduleDaysLayout.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        scheduleDaysLayout.adapter = ScheduleDaysLayoutAdapter(context!!, days)
        scheduleDaysLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val view = scheduleDaysLayout.getChildAt(defaultItemFocusId)
            val viewHolder = scheduleDaysLayout.findContainingViewHolder(view)
            viewHolder?.itemView?.requestFocus()
        }


        // Получение расписания из сервиса
        thread {
            var text : String
            try {
                val scheduleJsonString = MainActivity.sendGet("https://my-json-server.typicode.com/AntonScript/schedule-service/GroupStudent")
                val schedule = GsonBuilder().create().fromJson(scheduleJsonString, Schedule::class.java)
                text = GsonBuilder().create().toJson(schedule)
            } catch (e : Exception) {
                text = e.toString()
            }
            MainActivity.mHandler.post {
                //scheduleTestTV?.text = text
            }
        }
    }

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
}