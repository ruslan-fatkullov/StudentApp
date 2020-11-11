package com.example.studentass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentass.R
import com.example.studentass.adapters.ScheduleDaysLayoutAdapter
import com.example.studentass.adapters.ScheduleDaysLayoutItem
import com.example.studentass.adapters.SchedulePairsRvAdapter
import com.example.studentass.adapters.SchedulePairsRvItem
import com.example.studentass.models.Schedule
import kotlinx.android.synthetic.main.fragment_schedule.*

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

        var defaultItemFocusId = 3
        var days = ArrayList<ScheduleDaysLayoutItem>()
        days.add(ScheduleDaysLayoutItem("ПН", "0"))
        days.add(ScheduleDaysLayoutItem("ВТ", "0"))
        days.add(ScheduleDaysLayoutItem("СР", "0"))
        days.add(ScheduleDaysLayoutItem("ЧТ", "0"))
        days.add(ScheduleDaysLayoutItem("ПТ", "0"))
        days.add(ScheduleDaysLayoutItem("СБ", "0"))
        days.add(ScheduleDaysLayoutItem("ВС", "0"))
        scheduleDaysRv.hasFixedSize()
        scheduleDaysRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        scheduleDaysRv.adapter = ScheduleDaysLayoutAdapter(context!!, days)
        scheduleDaysRv.viewTreeObserver.addOnGlobalLayoutListener {
            val view = scheduleDaysRv.getChildAt(defaultItemFocusId)
            val viewHolder = scheduleDaysRv.findContainingViewHolder(view)
            viewHolder?.itemView?.requestFocus()
        }

        weekNum = 1
        weekTv.text = "Неделя $weekNum"

        var pairs = ArrayList<SchedulePairsRvItem>()
        pairs.add(SchedulePairsRvItem("Основы автоматики", "08:00 - 09:30", "3-312","Игонин А.Г.", "Лекция", "пуцщлпшоцшщуогшпорцшугцпу"))
        pairs.add(SchedulePairsRvItem("Основы автоматики", "09:40 - 11:10", "3-312","Игонин А.Г.", "Лекция", "пшоукшпошщуаопшоаошпаошща"))
        pairs.add(SchedulePairsRvItem("Организация ЭВМ и систем", "11:30 - 13:00", "3-308","Лылова А.В.", "Лабораторная работа", "уалщцлуцщаошцщуаогшщрагшуарцшцу"))
        schedulePairsRv.hasFixedSize()
        schedulePairsRv.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        schedulePairsRv.adapter = SchedulePairsRvAdapter(context!!, pairs)



    }

    fun switchWeek() {
        if (weekNum == 1) weekNum = 2
        else weekNum = 1
        var text = "Неделя $weekNum"
        weekTv.text = text
    }


    fun onPreviousWeekBnClick(view: View){
        switchWeek()

    }
    fun onNextWeekBnClick(view: View){
        switchWeek()

    }
}