package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.fragments.QuestionTypeSelectFragment
import com.example.studentass.models.TestAnswer
import kotlinx.android.synthetic.main.select_question_item.view.*

class QuestionSelectRvAdapter(private val context: Context) : RecyclerView.Adapter<QuestionSelectRvAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val select_item = view.select_item
        val jopa = view.answer_text
        fun bind(itemData: TestAnswer, context: Context, clicked: Boolean) {
            jopa.text = itemData.answer
            var drawable : Drawable?
            if(clicked){
                drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.select_answer_item_background_cliked)!!)
            }else{
                drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.select_answer_item_background)!!)
            }
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            select_item.background = drawable

        }

    }

    var dataList = ArrayList<TestAnswer>()
    var clicked = arrayListOf<Boolean>()



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionSelectRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return QuestionSelectRvAdapter.ViewHolder(inflater.inflate(R.layout.select_question_item, parent, false))
    }

    override fun onBindViewHolder(holder: QuestionSelectRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        val cl = clicked[position]
        holder.bind(itemData, context, cl)
        holder.select_item.setOnClickListener {
            if (cl) {
                QuestionTypeSelectFragment.answ.remove(dataList[holder.adapterPosition].id)
                clicked[holder.adapterPosition] = false
            } else {
                QuestionTypeSelectFragment.answ.add(dataList[holder.adapterPosition].id)
                clicked[holder.adapterPosition] = true
            }
            notifyItemChanged(holder.adapterPosition)
        }


    }



    override fun getItemCount(): Int = dataList.size



}