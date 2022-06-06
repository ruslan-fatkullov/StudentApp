package com.example.studentass.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.WorkModel
import kotlinx.android.synthetic.main.chat_message_item.view.*

class ChatRvAdapter(private val context: Context) :
    RecyclerView.Adapter<ChatRvAdapter.ViewHolder>() {


    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val messageStudent = view.student_message
        private val messageTeacher = view.teacher_message
        private val student_message_CL = view.student_message_CL
        private val student_message_CLCL = view.student_message_CLCL
        private val teacher_message_CL = view.teacher_message_CL
        private val teacher_message_CLCL = view.teacher_message_CLCL

        fun bind(itemData: WorkModel, context: Context) {
            messageStudent.text = itemData.studentComment
            messageTeacher.text = itemData.teacherComment

            student_message_CL.background = ContextCompat.getDrawable(context, R.drawable.ic_student_mess_back)
            teacher_message_CL.background = ContextCompat.getDrawable(context, R.drawable.ic_teacher_mess_back)

            if(itemData.studentComment.isEmpty()){
                student_message_CLCL.visibility = View.GONE
            }else if(itemData.teacherComment.isEmpty()){
                teacher_message_CLCL.visibility = View.GONE
            }

        }


    }

    var dataList = ArrayList<WorkModel>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(
            inflater.inflate(R.layout.chat_message_item, parent, false),
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)

    }

    override fun getItemCount(): Int = dataList.size


}


