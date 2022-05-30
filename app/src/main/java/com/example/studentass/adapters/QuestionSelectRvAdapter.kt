package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.fragments.QuestionTypeSelectFragment
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.select_question_item.view.*

class QuestionSelectRvAdapter(private val context: Context) :
    RecyclerView.Adapter<QuestionSelectRvAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectItem: ConstraintLayout = view.select_item
        private val answerText: TextView = view.answer_text
        fun bind(itemData: TestAnswer, context: Context, clicked: Boolean) {
            answerText.text = itemData.answer
            val drawable: Drawable? = if (clicked) {
                DrawableCompat.wrap(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.select_answer_item_background_cliked
                    )!!
                )
            } else {
                DrawableCompat.wrap(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.select_answer_item_background
                    )!!
                )
            }
            if (drawable != null) {
                DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            }
            selectItem.background = drawable

        }

    }

    var dataList = ArrayList<TestAnswer>()
    var clicked = arrayListOf<Boolean>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.select_question_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        val cl = clicked[position]
        holder.bind(itemData, context, cl)
        holder.selectItem.setOnClickListener {
            if (cl) {
                QuestionTypeSelectFragment.answer.remove(dataList[holder.adapterPosition].id)
                clicked[holder.adapterPosition] = false
            } else {
                QuestionTypeSelectFragment.answer.add(dataList[holder.adapterPosition].id)
                clicked[holder.adapterPosition] = true
            }
            notifyItemChanged(holder.adapterPosition)
        }


    }


    override fun getItemCount(): Int = dataList.size


}