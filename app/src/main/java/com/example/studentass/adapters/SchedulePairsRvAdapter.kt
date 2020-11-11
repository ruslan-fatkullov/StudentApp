package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.ScheduleDayCouple
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import kotlinx.android.synthetic.main.schedule_pair_rv_item.view.*

class SchedulePairsRvAdapter (val context : Context, val schedulePairsRvItems: List<ScheduleDayCouple>) : RecyclerView.Adapter<SchedulePairsRvAdapter.ViewHolder>(){
    class ViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        private val scheduleTimes = listOf<String>(
            "08:00 - 09:30",
            "09:40 - 11:10",
            "11:30 - 13:00",
            "13:10 - 14:40",
            "14:50 - 16:20",
            "16:30 - 18:00",
            "18:10 - 19:40",
            "19:50 - 21:20"
        )
        private val pairTypes = listOf<String>(
            "Практика",
            "Лекция",
            "Лабораторная работа"
        )

        //val dayOfWeekTextView = view.dayOfWeekTextView
        //val dayTextView = view.dayTextView
        val pairNameTv: TextView? = view.pairNameTv
        val timeTv: TextView? = view.timeTv
        val locationTv: TextView? = view.locationTv
        val teacherNameTv: TextView? = view.teacherNameTv
        val pairLayout: ConstraintLayout? = view.pairLayout
        val pairTypeTv: TextView? = view.pairTypeTv

        fun bind(pair: ScheduleDayCouple, context: Context){
            pairNameTv?.text = "${pair.subject} ${if (pair.subgroup != 0) (", ${pair.subgroup} пг") else ("")}"
            timeTv?.text = scheduleTimes[pair.pair_number - 1]
            locationTv?.text = pair.place
            teacherNameTv?.text = pair.teacher
            pairTypeTv?.text = pairTypes[pair.typeSubject - 1]
            itemView.setOnClickListener(View.OnClickListener { v: View? ->
                run {
                    Toast.makeText(context, pair.info, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return  ViewHolder(inflater.inflate(R.layout.schedule_pair_rv_item, parent, false))
    }

    override fun getItemCount(): Int {
        return schedulePairsRvItems.size
    }

    override fun onBindViewHolder(holder: SchedulePairsRvAdapter.ViewHolder, position: Int) {
        var pairItem = schedulePairsRvItems.get(position)
        holder.bind(pairItem, context)
    }

}