package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import kotlinx.android.synthetic.main.schedule_days_layout_item.view.*

class ScheduleDaysLayoutAdapter (context : Context, scheduleDaysLayoutItems: ArrayList<ScheduleDaysLayoutItem>) : RecyclerView.Adapter<ScheduleDaysLayoutAdapter.ViewHolder>() {
    class ViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        val dayOfWeekTextView = view.dayOfWeekTextView
        val dayTextView = view.dayTextView

        fun bind(daysItem: ScheduleDaysLayoutItem, context: Context){
            dayOfWeekTextView.text = daysItem.dayOfWeek
            dayTextView.text = daysItem.day

            itemView.setOnClickListener{
                Toast.makeText(context, "Pressed ${dayOfWeekTextView.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var daysItems = scheduleDaysLayoutItems
    var contextR = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleDaysLayoutAdapter.ViewHolder {
        val inflater = LayoutInflater.from(contextR)
        return  ViewHolder(inflater.inflate(R.layout.schedule_days_layout_item, parent, false))
    }

    override fun getItemCount(): Int {
        return daysItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var daysItem = daysItems.get(position)
        holder.bind(daysItem, contextR)
    }
}