package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.ScheduleDayCouple
import kotlinx.android.synthetic.main.schedule_pair_rv_item.view.*

class SchedulePairsRvAdapter (private val context : Context, private val schedulePairsRvItems: List<ScheduleDayCouple>) : RecyclerView.Adapter<SchedulePairsRvAdapter.ViewHolder>(){
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val pairNameTv: TextView? = view.pairNameTv
        private val timeTv: TextView? = view.timeTv
        private val locationTv: TextView? = view.locationTv
        private val teacherNameTv: TextView? = view.teacherNameTv
        //private val pairLayout: ConstraintLayout? = view.pairLayout
        private val pairTypeTv: TextView? = view.pairTypeTv

        fun bind(pair: ScheduleDayCouple, context: Context){
            val pairNameTvText = "${pair.subject} ${if (pair.subgroup != 0) (", ${pair.subgroup} пг") else ("")}"
            pairNameTv?.text = pairNameTvText

            timeTv?.text = when (pair.pair_number) {
                1 -> "08:00 - 09:30"
                2 -> "09:40 - 11:10"
                3 -> "11:30 - 13:00"
                4 -> "13:10 - 14:40"
                5 -> "14:50 - 16:20"
                6 -> "16:30 - 18:00"
                7 -> "18:10 - 19:40"
                8 -> "19:50 - 21:20"
                else -> "Error"
            }

            locationTv?.text = pair.place

            teacherNameTv?.text = pair.teacher

            pairTypeTv?.text = when (pair.typeSubject) {
                1 -> "Практика"
                2 -> "Лекция"
                3 -> "Лабораторная работа"
                else -> "Error"
            }

            itemView.setOnClickListener {
                run {
                    Toast.makeText(context, pair.info, Toast.LENGTH_SHORT).show()
                }
            }
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pairItem = schedulePairsRvItems[position]
        holder.bind(pairItem, context)
    }
}