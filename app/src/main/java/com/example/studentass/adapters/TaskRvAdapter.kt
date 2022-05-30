package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.TaskModel
import kotlinx.android.synthetic.main.task_layout_item_second.view.titleTask
import kotlinx.android.synthetic.main.task_layout_item_second.view.typeTask
import kotlinx.android.synthetic.main.task_layout_item_second.view.*

class TaskRvAdapter(private val context: Context) :
    RecyclerView.Adapter<TaskRvAdapter.ViewHolder>() {


    private lateinit var mListener: OnClickListener

    class ViewHolder(view: View, private var mListener: OnClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private val titleTask: TextView = view.titleTask
        private val typeTask: TextView = view.typeTask
        val mainTaskLayoutSecond: ConstraintLayout = view.mainTaskLayoutSecond


        fun bind(itemData: TaskModel, context: Context) {


            val drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.work_background_item_selector
                )!!
            )
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(context, R.color.colorNotificationBackground)
            )
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            mainTaskLayoutSecond.background = drawable


            val drawable1 = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context, when (itemData.type) {
                        "LAB" -> R.drawable.task_type_laboratory
                        else -> R.drawable.task_type_practice
                    }
                )!!
            )
            DrawableCompat.setTintMode(drawable1, PorterDuff.Mode.SRC_ATOP)
            typeTask.background = drawable1

            titleTask.text = itemData.title
            typeTask.text = when (itemData.type) {
                "LAB" -> "Лабораторная"
                "PRACTICE" -> "Практическая"
                "ESSAY" -> "Эссе"
                else -> ({}).toString()
            }


        }

        init {
            mainTaskLayoutSecond.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.setOnClickListener(adapterPosition)
        }

    }

    var dataList = ArrayList<TaskModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(
            inflater.inflate(R.layout.task_layout_item_second, parent, false),
            mListener
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.mainTaskLayoutSecond.setOnClickListener {
            mListener.setOnClickListener(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int = dataList.size
    interface OnClickListener {
        fun setOnClickListener(position: Int)
    }

    fun setOnClickListener(mListener: OnClickListener) {
        this.mListener = mListener
    }

}