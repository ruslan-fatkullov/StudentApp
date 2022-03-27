package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.TaskModel
import com.example.studentass.models.TestThemesData
import kotlinx.android.synthetic.main.literature_layout_item.view.*
import kotlinx.android.synthetic.main.task_layout_item.view.*
import kotlinx.android.synthetic.main.test_layout_item.view.*

class TaskRvAdapter(private val context: Context) : RecyclerView.Adapter<TaskRvAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val titleTask = view.titleTask
        val descriptionTask = view.descriptionTask
        val typeTask = view.typeTask
        val mainTaskLayout = view.mainTaskLayout

        //val typeOfTask = when(itemView.)


        fun bind(itemData: TaskModel, context: Context) {

            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.task_background)!!)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, when (itemData.type) {
                "LAB" -> R.color.colorTaskLaboratoryBackground
                else -> R.color.colorTaskPracticeBackground
            }))
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            mainTaskLayout.background = drawable


            val taskTypeColor = when (itemData.type) {
                "LAB" -> R.color.colorTaskLaboratoryBackground
                else -> R.color.colorTaskPracticeBackground
            }

            titleTask.text = itemData.title
            descriptionTask.text = itemData.description
            typeTask.text = when(itemData.type){
                "LAB" -> "Лабораторная"
                "PRACTICE" -> "Практика"
                else -> ({}).toString()
            }
            typeTask.setTextColor(ContextCompat.getColor(context, taskTypeColor))
        }

//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            if (mListner != null){
//                mListner.setOnClickListener(adapterPosition)
//            }
//        }
    }

    var dataList = ArrayList<TaskModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return TaskRvAdapter.ViewHolder(inflater.inflate(R.layout.task_layout_item, parent, false))
    }




    override fun onBindViewHolder(holder: TaskRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
//        holder.itemView.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                if (mListner != null){
//                    mListner.setOnClickListener(holder.getAdapterPosition())
//                }
//            }
//        })


    }

    override fun getItemCount(): Int = dataList.size


//    override fun getItemCount(): Int = dataList.size
//    interface onItemClickListener{
//        fun setOnClickListener(position: Int)
//    }
//    fun setOnItemClickListener(mListner: onItemClickListener) {
//        this.mListner = mListner
//    }

}