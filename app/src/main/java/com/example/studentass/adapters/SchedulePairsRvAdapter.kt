package com.example.studentass.adapters

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.ScheduleDayCouple
import kotlinx.android.synthetic.main.schedule_pair_rv_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class SchedulePairsRvAdapter (private val context : Context) : RecyclerView.Adapter<SchedulePairsRvAdapter.ViewHolder>(){
    class PairTime (pairNum: Int) {
        var beginHour = when (pairNum) {
            1 -> 8
            2 -> 9
            3 -> 11
            4 -> 13
            5 -> 14
            6 -> 16
            7 -> 18
            8 -> 19
            else -> -1
        }
        var beginMinute = when (pairNum) {
            1 -> 0
            2 -> 40
            3 -> 30
            4 -> 10
            5 -> 50
            6 -> 30
            7 -> 10
            8 -> 50
            else -> -1
        }
        var endHour = when (pairNum) {
            1 -> 9
            2 -> 11
            3 -> 13
            4 -> 14
            5 -> 16
            6 -> 18
            7 -> 19
            8 -> 21
            else -> -1
        }
        var endMinute = when (pairNum) {
            1 -> 30
            2 -> 10
            3 -> 0
            4 -> 40
            5 -> 20
            6 -> 0
            7 -> 40
            8 -> 20
            else -> -1
        }
        var intervalString = when (pairNum) {
            1 -> "08:00 - 09:30"
            2 -> "09:40 - 11:10"
            3 -> "11:30 - 13:00"
            4 -> "13:10 - 14:40"
            5 -> "14:50 - 16:20"
            6 -> "16:30 - 18:00"
            7 -> "18:10 - 19:40"
            8 -> "19:50 - 21:20"
            else -> "Error"
        }
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val pairNameTv: TextView? = view.pairNameTv
        private val timeTv: TextView? = view.timeTv
        private val locationTv: TextView? = view.locationTv
        private val teacherNameTv: TextView? = view.teacherNameTv

        private val pairLayout: ConstraintLayout? = view.pairLayout
        private val timeIv: ImageView? = view.timeIv
        private val locationIv: ImageView? = view.locationIv
        private val teacherNameIv: ImageView? = view.teacherNameIv
        private val pairTypeTv: TextView? = view.pairTypeTv

        fun bind(pair: ScheduleDayCouple, context: Context, dataYear: Int, dataDayOfYear: Int){
            val currentCalendar = Calendar.getInstance()
            val pairTime = PairTime(pair.pair_number)
            val pairCalendar = Calendar.getInstance()
            pairCalendar.set(Calendar.YEAR, dataYear)
            pairCalendar.set(Calendar.DAY_OF_YEAR, dataDayOfYear)

            val pairNameTvText = "${pair.subject} ${if (pair.subgroup != 0) (", ${pair.subgroup} пг") else ("")}"
            pairNameTv?.text = pairNameTvText

            val timeText = "${pair.pair_number} пара, ${pairTime.intervalString}"
            timeTv?.text = timeText

            locationTv?.text = pair.place

            teacherNameTv?.text = pair.teacher

            val pairTypeText = when (pair.typeSubject) {
                1 -> "Практика"
                2 -> "Лекция"
                3 -> "Лаб"
                else -> "Error"
            }
            val pairTypeColor = when (pair.typeSubject) {
                1 -> R.color.colorSchedulePairTypePractice
                2 -> R.color.colorSchedulePairTypeLection
                3 -> R.color.colorSchedulePairTypeLab
                else -> R.color.colorPrimary
            }
            val pairTypeBackgroundColor = when (pair.typeSubject) {
                1 -> R.color.colorSchedulePairTypeBackgroundPractice
                2 -> R.color.colorSchedulePairTypeBackgroundLection
                3 -> R.color.colorSchedulePairTypeBackgroundLab
                else -> R.color.colorPrimary
            }

            pairTypeTv?.text = pairTypeText
            pairTypeTv?.setTextColor(ContextCompat.getColor(context, pairTypeColor))
            val shapeDrawable: GradientDrawable? = pairTypeTv?.background as GradientDrawable
            shapeDrawable?.setColor(ContextCompat.getColor(context, pairTypeBackgroundColor))

            itemView.setOnClickListener {
                run {
                    Toast.makeText(context, pair.info, Toast.LENGTH_SHORT).show()
                }
            }

            pairCalendar.set(Calendar.HOUR_OF_DAY, pairTime.endHour)
            pairCalendar.set(Calendar.MINUTE, pairTime.endMinute)
            if (pairCalendar > currentCalendar) {
                pairLayout?.background = when (pair.typeSubject) {
                    1 -> ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_background_practice)
                    2 -> ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_background_lection)
                    3 -> ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_background_lab)
                    else -> null
                }
                timeIv?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_ico_time))
                locationIv?.setColorFilter(ContextCompat.getColor(context, pairTypeColor))
                teacherNameIv?.setColorFilter(ContextCompat.getColor(context, pairTypeColor))


                pairCalendar.set(Calendar.HOUR_OF_DAY, pairTime.beginHour)
                pairCalendar.set(Calendar.MINUTE, pairTime.beginHour)
                if (pairCalendar < currentCalendar) {
                    timeIv?.setColorFilter(ContextCompat.getColor(context, pairTypeColor))
                }
                else {
                    timeIv?.setColorFilter(ContextCompat.getColor(context, R.color.colorSchedulePairTypeAnyDone))
                }
            }
            else {
                pairLayout?.background = ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_background_any_done)

                timeIv?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_im_schedule_pair_ico_time_done))
                timeIv?.setColorFilter(ContextCompat.getColor(context, pairTypeColor))
                locationIv?.setColorFilter(ContextCompat.getColor(context, R.color.colorSchedulePairTypeAnyDone))
                teacherNameIv?.setColorFilter(ContextCompat.getColor(context, R.color.colorSchedulePairTypeAnyDone))
            }
        }
    }
    var dataList = ArrayList<ScheduleDayCouple>()
    var dataYear: Int = 0
    var dataDayOfYear: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return  ViewHolder(inflater.inflate(R.layout.schedule_pair_rv_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pairItem = dataList[position]
        holder.bind(pairItem, context, dataYear, dataDayOfYear)
    }
}