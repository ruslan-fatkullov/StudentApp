package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.match_question_item.view.*

class QuestionMatchRvAdapter(private val context: Context) : RecyclerView.Adapter<QuestionMatchRvAdapter.ViewHolder>() {

    private lateinit var mListner: onItemClickListener
    class ViewHolder(view: View, var mListner: QuestionMatchRvAdapter.onItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {

        val answerKeyMatchCl = view.answer_key_match_Cl
        val answerValueMatchCl = view.answer_value_match_Cl
        val matchItemUp = view.matchItemUp
        val matchItemDown = view.matchItemDown
        val answer_value_match = view.answer_value_match
        val answer_key_match = view.answer_key_match



        fun bind(itemData: TestAnswer, context: Context, key: String, valy: String) {
            var drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.select_answer_item_background)!!)
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            answerKeyMatchCl.background = drawable
            answerValueMatchCl.background = drawable
            answer_value_match.text = key
            answer_key_match.text = valy
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

    var dataList = ArrayList<TestAnswer>()
    var valList = arrayListOf<String>("1", "2", "3")
    var keyList = arrayListOf<String>("1", "2", "3")



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionMatchRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return QuestionMatchRvAdapter.ViewHolder(inflater.inflate(R.layout.match_question_item, parent, false), mListner)
    }

    override fun onBindViewHolder(holder: QuestionMatchRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        val key = keyList[position]
        val valy = keyList[position]
        holder.bind(itemData, context, key, valy)
        holder.matchItemUp.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //val pos = dataList[holder.adapterPosition]
                val pos = keyList[holder.adapterPosition]
                if (holder.adapterPosition != 0){
//                    dataList[holder.adapterPosition] = dataList[holder.adapterPosition - 1]
//                    dataList[holder.adapterPosition - 1] = pos
                    keyList[holder.adapterPosition] = keyList[holder.adapterPosition - 1]
                    keyList[holder.adapterPosition - 1] = pos
                    notifyItemChanged(holder.adapterPosition)
                    notifyItemChanged(holder.adapterPosition-1)
                }
            }
        })
        holder.matchItemDown.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //val pos = dataList[holder.adapterPosition]
                val pos = keyList[holder.adapterPosition]
                if (holder.adapterPosition < dataList.size-1){
                    keyList[holder.adapterPosition] = keyList[holder.adapterPosition + 1]
                    keyList[holder.adapterPosition + 1] = pos
                    notifyItemChanged(holder.adapterPosition)
                    notifyItemChanged(holder.adapterPosition+1)
                }
            }
        })

    }

    override fun getItemCount(): Int = dataList.size

    interface onItemClickListener{
        fun setOnClickListener(position: Int)
    }
    fun setOnItemClickListener(mListner:onItemClickListener){
        this.mListner = mListner
    }

}