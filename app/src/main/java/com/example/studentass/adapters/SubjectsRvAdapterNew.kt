package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.Subject
import kotlinx.android.synthetic.main.subjects_overview_item_new.view.*

class SubjectsRvAdapterNew(private val context: Context) :
    RecyclerView.Adapter<SubjectsRvAdapterNew.ViewHolder>() {

    private var literFragment: Fragment? = null
    private var taskFragment: Fragment? = null
    private var testFragment: Fragment? = null
    private var currentFrag: Fragment? = null

    companion object {

        var subFragmentManager = arrayListOf<FragmentManager>()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val nameSub = view.nameSub

        fun bind(itemData: Subject, context: Context, position: Int) {
            nameSub.text = "itemData.name"
        }

//        init {
//            itemView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View?) {
//            if (mListner != null) {
//                mListner.setOnClickListener(adapterPosition)
//            }
//        }


    }

    var dataList = ArrayList<Subject>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectsRvAdapterNew.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return SubjectsRvAdapterNew.ViewHolder(
            inflater.inflate(
                R.layout.subjects_overview_item_new,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context, position)
    }

    override fun getItemCount(): Int = dataList.size

//    interface onItemClickListener {
//        fun setOnClickListener(position: Int)
//    }
//
//    fun setOnItemClickListener(mListner: onItemClickListener) {
//        this.mListner = mListner
//    }
}

