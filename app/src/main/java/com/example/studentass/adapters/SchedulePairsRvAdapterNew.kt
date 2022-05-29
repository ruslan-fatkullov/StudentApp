package com.example.studentass.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.ScheduleNew.TimetableLesson
import kotlinx.android.synthetic.main.schedule_pair_rv_item_new.view.*
import kotlin.collections.ArrayList

class SchedulePairsRvAdapterNew(private val context: Context) :
    RecyclerView.Adapter<SchedulePairsRvAdapterNew.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener
    class PairTime(pairNum: Int) {
        var intervalString = when (pairNum) {
            1 -> "08:30 - 09:30"
            2 -> "10:00 - 11:20"
            3 -> "11:30 - 12:50"
            4 -> "13:30 - 14:50"
            5 -> "15:00 - 16:20"
            6 -> "16:30 - 17:50"
            7 -> "18:00 - 19:20"
            8 -> "19:30 - 20:50"
            else -> "Error"
        }
    }

    class ViewHolder(view: View, private var mListener: OnItemClickListener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val pairNameTv: TextView = view.pairNameTv
        private val timeTv: TextView = view.timeTv
        private val locationTv: TextView = view.locationTv
        private val teacherNameTv: TextView = view.teacherNameTv

        val pairLayout: ConstraintLayout = view.pairLayout
        private val pairTypeTv: TextView = view.pairTypeTv

        fun bind(
            pair: ArrayList<TimetableLesson>,
            pairNumber: Int,
            context: Context
        ) {
            if (pair.size != 0) {
                teacherNameTv.text = pair[0].teacher
                locationTv.text = pair[0].room
                val pairTime = PairTime(pairNumber)
                val timeText =
                    "$pairNumber ${context.getString(R.string.schedule_pair)}, ${pairTime.intervalString}"
                timeTv.text = timeText

                val typePair = pair[0].nameOfLesson.split(".")
                pairNameTv.text = typePair[1]


                val pairTypeText = when (typePair[0]) {
                    "пр" -> context.getString(R.string.schedule_pair_practice)
                    "лек" -> context.getString(R.string.schedule_pair_lection)
                    else -> context.getString(R.string.schedule_pair_lab)
                }
                val pairTypeColor = when (typePair[0]) {
                    "пр" -> R.color.colorSchedulePairTypePractice
                    "лек" -> R.color.colorSchedulePairTypeLection
                    else -> R.color.colorSchedulePairTypeLab
                }
                val pairTypeBackgroundColor = when (typePair[0]) {
                    "пр" -> R.color.colorSchedulePairTypeBackgroundPractice
                    "лек" -> R.color.colorSchedulePairTypeBackgroundLection
                    else -> R.color.colorSchedulePairTypeBackgroundLab
                }
                pairTypeTv.text = pairTypeText
                pairTypeTv.setTextColor(ContextCompat.getColor(context, pairTypeColor))
                val shapeDrawable: GradientDrawable = pairTypeTv.background as GradientDrawable
                shapeDrawable.setColor(ContextCompat.getColor(context, pairTypeBackgroundColor))

                pairLayout.background = when (typePair[0]) {
                    "пр"  -> ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_im_schedule_pair_background_practice
                    )
                    "лек" -> ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_im_schedule_pair_background_lection
                    )
                    else -> ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_im_schedule_pair_background_lab
                    )
                }

            }


        }

        init {
            pairLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.setOnClickListener(adapterPosition)
        }
    }

    private var pair = ArrayList<TimetableLesson>()
    var dataList = arrayListOf(pair)
    var pairNumber: Int = 0
    var pairNumberArray = arrayListOf(pairNumber)

    var dataYear: Int = 0
    var dataDayOfYear: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(inflater.inflate(R.layout.schedule_pair_rv_item_new, parent, false), mListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pairItem = dataList[position]
        val pairNumber = pairNumberArray[position]
        holder.bind(pairItem, pairNumber, context)
        holder.pairLayout.setOnClickListener {
            mListener.setOnClickListener(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = dataList.size
    interface OnItemClickListener{
        fun setOnClickListener(position: Int)
    }
    fun setOnItemClickListener(mListner: OnItemClickListener) {
        this.mListener = mListner
    }

}