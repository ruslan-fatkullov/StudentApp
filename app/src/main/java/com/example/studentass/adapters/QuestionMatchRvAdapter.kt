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
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.match_question_item.view.*

class QuestionMatchRvAdapter(private val context: Context) :
    RecyclerView.Adapter<QuestionMatchRvAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    class ViewHolder(view: View, private var mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private val answerKeyMatchCl: ConstraintLayout = view.answer_key_match_Cl
        private val answerValueMatchCl: ConstraintLayout = view.answer_value_match_Cl
        val matchItemUp: FrameLayout = view.matchItemUp
        val matchItemDown: FrameLayout = view.matchItemDown
        private val answerValueMatch: TextView = view.answer_value_match
        private val answerKeyMatch: TextView = view.answer_key_match


        fun bind(context: Context, key: String, valley: String) {
            val drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.select_answer_item_background
                )!!
            )
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            answerKeyMatchCl.background = drawable
            answerValueMatchCl.background = drawable
            answerValueMatch.text = key
            answerKeyMatch.text = valley
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.setOnClickListener(adapterPosition)
        }
    }

    var dataList = ArrayList<TestAnswer>()
    private var keyList = arrayListOf("1", "2", "3")


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.match_question_item, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataList[position]
        val key = keyList[position]
        val valley = keyList[position]
        holder.bind(context, key, valley)
        holder.matchItemUp.setOnClickListener {
            val pos = keyList[holder.adapterPosition]
            if (holder.adapterPosition != 0) {
                keyList[holder.adapterPosition] = keyList[holder.adapterPosition - 1]
                keyList[holder.adapterPosition - 1] = pos
                notifyItemChanged(holder.adapterPosition)
                notifyItemChanged(holder.adapterPosition - 1)
            }
        }
        holder.matchItemDown.setOnClickListener { //val pos = dataList[holder.adapterPosition]
            val pos = keyList[holder.adapterPosition]
            if (holder.adapterPosition < dataList.size - 1) {
                keyList[holder.adapterPosition] = keyList[holder.adapterPosition + 1]
                keyList[holder.adapterPosition + 1] = pos
                notifyItemChanged(holder.adapterPosition)
                notifyItemChanged(holder.adapterPosition + 1)
            }
        }

    }

    override fun getItemCount(): Int = dataList.size

    interface OnItemClickListener {
        fun setOnClickListener(position: Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }

}