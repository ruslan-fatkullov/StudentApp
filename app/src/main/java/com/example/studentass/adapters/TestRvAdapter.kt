package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.TestThemesData
import kotlinx.android.synthetic.main.literature_layout_item.view.*

class TestRvAdapter (private val context: Context) : RecyclerView.Adapter<TestRvAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val type = view.typeOfLiterature
        val name = view.nameOfLiterature
        val aut = view.authorOfLiterature
        val desc = view.descriptionOfLiterature

        fun bind(itemData: TestThemesData, context: Context) {
            type.text = "fsd"
            name.text = itemData.name
            desc.text = itemData.decryption
            aut.text = itemData.createdAt.toString()
        }
    }

    var dataList = ArrayList<TestThemesData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return TestRvAdapter.ViewHolder(inflater.inflate(R.layout.literature_layout_item, parent, false))
    }




    override fun onBindViewHolder(holder: TestRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)


    }



    override fun getItemCount(): Int = dataList.size
}