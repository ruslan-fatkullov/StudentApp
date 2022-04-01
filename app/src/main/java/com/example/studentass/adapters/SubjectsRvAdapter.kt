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
import com.example.studentass.models.Subject
import kotlinx.android.synthetic.main.subjects_overview_item.view.*

class SubjectsRvAdapter (private val context: Context) : RecyclerView.Adapter<SubjectsRvAdapter.ViewHolder>() {

    private lateinit var mListner: onItemClickListener
    class ViewHolder(view: View, var mListner: onItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val subjectNameTv = view.subjectNameTv
        val subjectLayout = view.subjectLayout
        val numberOfSubject = view.numberOfSubject

        fun bind(itemData: Subject, context: Context, position: Int) {
            subjectNameTv.text = itemData.name
            numberOfSubject.text = "#${position+1}"
            var drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.subject_item_background)!!)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            subjectLayout.background = drawable

        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (mListner != null){
                mListner.setOnClickListener(adapterPosition)
            }
        }


    }

    var dataList = ArrayList<Subject>()



    override fun onCreateViewHolder (
        parent: ViewGroup,
        viewType: Int
        ): SubjectsRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return SubjectsRvAdapter.ViewHolder(inflater.inflate(R.layout.subjects_overview_item, parent, false), mListner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context, position)
    }

    override fun getItemCount(): Int = dataList.size

    interface onItemClickListener{
        fun setOnClickListener(position: Int)
    }
    fun setOnItemClickListener(mListner:onItemClickListener){
        this.mListner = mListner
    }
}

