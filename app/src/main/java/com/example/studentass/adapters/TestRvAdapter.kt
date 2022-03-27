package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.LiteratureData
import com.example.studentass.models.TestThemesData
import kotlinx.android.synthetic.main.literature_layout_item.view.*
import kotlinx.android.synthetic.main.task_layout_item.view.*
import kotlinx.android.synthetic.main.test_layout_item.view.*

class TestRvAdapter(private val context: Context) : RecyclerView.Adapter<TestRvAdapter.ViewHolder>() {


    private lateinit var mListner: onItemClickListener
    class ViewHolder(view: View, var mListner: onItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {

        val nameOfTestV = view.nameOfTestV
        val descriptionOfTest = view.descriptionOfTest

        //val typeOfTask = when(itemView.)


        fun bind(itemData: TestThemesData, context: Context) {
            nameOfTestV.text = itemData.name
            descriptionOfTest.text = itemData.decryption
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

    var dataList = ArrayList<TestThemesData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return TestRvAdapter.ViewHolder(inflater.inflate(R.layout.test_layout_item, parent, false), mListner)
    }




    override fun onBindViewHolder(holder: TestRvAdapter.ViewHolder, position: Int) {
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