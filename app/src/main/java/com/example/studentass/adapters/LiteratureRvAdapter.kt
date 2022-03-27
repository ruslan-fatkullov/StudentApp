package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.LiteratureData
import kotlinx.android.synthetic.main.literature_layout_item.view.*

class LiteratureRvAdapter(private val context: Context) : RecyclerView.Adapter<LiteratureRvAdapter.ViewHolder>() {


    private lateinit var mListner: onItemClickListener
    class ViewHolder(view: View, var mListner: onItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {

        val typeOfLiterature = view.typeOfLiterature
        val nameOfLiterature = view.nameOfLiterature
        val authorOfLiterature = view.authorOfLiterature
        val descriptionOfLiterature = view.descriptionOfLiterature


        fun bind(itemData: LiteratureData, context: Context) {
            typeOfLiterature.text = when (itemData.type) {
                "WORKBOOK" -> "Учебное пособие:"
                "BOOK" -> "Книга:"
                else -> {""}
            }
            nameOfLiterature.text = itemData.title
            authorOfLiterature.text = "Автор: ${itemData.authors}"
            descriptionOfLiterature.text = "Описание: ${itemData.description}"
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

    var dataList = ArrayList<LiteratureData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LiteratureRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return LiteratureRvAdapter.ViewHolder(inflater.inflate(R.layout.literature_layout_item, parent, false), mListner)
    }




    override fun onBindViewHolder(holder: LiteratureRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (mListner != null){
                    mListner.setOnClickListener(holder.getAdapterPosition())
                }
            }
        })


    }



    override fun getItemCount(): Int = dataList.size
    interface onItemClickListener{
        fun setOnClickListener(position: Int)
    }
    fun setOnItemClickListener(mListner: onItemClickListener) {
        this.mListner = mListner
    }

}