package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.PassedTests
import kotlinx.android.synthetic.main.test_layout_item_second.view.*

class TestRvAdapter(private val context: Context) :
    RecyclerView.Adapter<TestRvAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    class ViewHolder(view: View, private var mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        val testItem: ConstraintLayout = view.test_item

        private val nameOfTestV: TextView = view.nameOfTestV
        private val passedTestsLabel: TextView = view.passedTestLabel


        fun bind(itemData: PassedTests, context: Context) {

            val drawable = DrawableCompat.wrap(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.work_background_item_selector
                )!!
            )
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(context, R.color.colorNotificationBackground)
            )
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
            testItem.background = drawable

            nameOfTestV.text = itemData.theme.name
            when (itemData.ratings.size) {
                0 -> passedTestsLabel.text = "Не пройден"
                else -> passedTestsLabel.text = "Пройден"
            }

            val textColor = when(itemData.ratings.size){
                0 -> R.color.testNotPassed
                else -> R.color.testPassed
            }
            passedTestsLabel.setTextColor(ContextCompat.getColor(context, textColor))


        }

        init {
            testItem.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.setOnClickListener(adapterPosition)
        }
    }

    var dataList = ArrayList<PassedTests>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(
            inflater.inflate(R.layout.test_layout_item_second, parent, false),
            mListener
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.testItem.setOnClickListener {
            mListener.setOnClickListener(holder.adapterPosition)
        }


    }


    override fun getItemCount(): Int = dataList.size
    interface OnItemClickListener {
        fun setOnClickListener(position: Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }

}