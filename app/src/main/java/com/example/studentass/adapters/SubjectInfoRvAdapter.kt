package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.AnswerOfWorks
import com.example.studentass.models.SubjectOverview
import kotlinx.android.synthetic.main.answer_of_works_item.view.*
import kotlinx.android.synthetic.main.fragment_subject_info.view.*
import kotlinx.android.synthetic.main.subjects_overview_item.view.*

class SubjectInfoRvAdapter(private val context: Context): RecyclerView.Adapter<SubjectInfoRvAdapter.ViewHolder>()  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //
        val subjectLiteratureLayout = view.subjectLiteratureLayout

        //

        fun bind(itemData: AnswerOfWorks, context: Context) {
            subjectLiteratureLayout.setOnClickListener(){
                Toast.makeText(context, "Literature", Toast.LENGTH_SHORT).show()
            }

        }

    }

    var dataList = ArrayList<AnswerOfWorks>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectInfoRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return SubjectInfoRvAdapter.ViewHolder(inflater.inflate(R.layout.answer_of_works_item, parent, false))
    }
    override fun onBindViewHolder(holder: SubjectInfoRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)

    }

    override fun getItemCount(): Int = dataList.size



}