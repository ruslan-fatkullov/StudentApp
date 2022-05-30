package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.fragments.QuestionTypeSequenceFragment
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.sequence_question_item.view.*

class QuestionSequenceRvAdapter(private val context: Context) :
    RecyclerView.Adapter<QuestionSequenceRvAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val seq: ConstraintLayout = view.sequence_item
        val up: FrameLayout = view.itemUp
        val down: FrameLayout = view.itemDown
        private val ans: TextView = view.answer_text_seq
        fun bind(itemData: TestAnswer, context: Context) {
            ans.text = itemData.answer
            val drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.select_answer_item_background
                )!!
            )
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            seq.background = drawable
        }

    }

    var dataList = ArrayList<TestAnswer>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.sequence_question_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.up.setOnClickListener {
            val pos = dataList[holder.adapterPosition]
            if (holder.adapterPosition != 0) {
                dataList[holder.adapterPosition] = dataList[holder.adapterPosition - 1]
                dataList[holder.adapterPosition - 1] = pos
                notifyItemChanged(holder.adapterPosition)
                notifyItemChanged(holder.adapterPosition - 1)

                updateDataList()

            }
        }
        holder.down.setOnClickListener {
            val pos = dataList[holder.adapterPosition]
            if (holder.adapterPosition < dataList.size - 1) {
                dataList[holder.adapterPosition] = dataList[holder.adapterPosition + 1]
                dataList[holder.adapterPosition + 1] = pos
                notifyItemChanged(holder.adapterPosition)
                notifyItemChanged(holder.adapterPosition + 1)

                updateDataList()
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    private fun updateDataList() {
        QuestionTypeSequenceFragment.answer.clear()
        for (i in dataList) {
            QuestionTypeSequenceFragment.answer.add(i.id)
        }
    }

}