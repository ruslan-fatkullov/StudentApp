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

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

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
    }

    var dataList = ArrayList<LiteratureData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LiteratureRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return LiteratureRvAdapter.ViewHolder(inflater.inflate(R.layout.literature_layout_item, parent, false))
    }




    override fun onBindViewHolder(holder: LiteratureRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)


    }



    override fun getItemCount(): Int = dataList.size

}