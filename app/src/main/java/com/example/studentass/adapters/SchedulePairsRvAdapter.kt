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
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*
import kotlinx.android.synthetic.main.schedule_pair_rv_item.view.*

class SchedulePairsRvAdapter (val context : Context, val schedulePairsRvItems: ArrayList<SchedulePairsRvItem>) : RecyclerView.Adapter<SchedulePairsRvAdapter.ViewHolder>(){
    class ViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        //val dayOfWeekTextView = view.dayOfWeekTextView
        //val dayTextView = view.dayTextView
        val pairNameTv: TextView? = view.pairNameTv
        val timeTv: TextView? = view.timeTv
        val locationTv: TextView? = view.locationTv
        val teacherNameTv: TextView? = view.teacherNameTv
        val pairLayout: ConstraintLayout? = view.pairLayout
        val pairTypeTv: TextView? = view.pairTypeTv

        fun bind(pairsRvItem: SchedulePairsRvItem, context: Context){
            //dayOfWeekTextView.text = daysItem.dayOfWeek
            //dayTextView.text = daysItem.day
            pairNameTv?.text = pairsRvItem.pairName
            timeTv?.text = pairsRvItem.time
            locationTv?.text = pairsRvItem.location
            teacherNameTv?.text = pairsRvItem.teacherName
            pairTypeTv?.text = pairsRvItem.pairType
            //pairLayout?.background = pairsRvItem.backgroundDrawable
            itemView.setOnClickListener(View.OnClickListener { v: View? ->
                run {
                    Toast.makeText(context, pairsRvItem.fullInfo, Toast.LENGTH_SHORT).show()
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