package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.LiteratureData
import kotlinx.android.synthetic.main.literature_layout_item.view.*

class LiteratureRvAdapter(private val context: Context) :
    RecyclerView.Adapter<LiteratureRvAdapter.ViewHolder>() {


    private lateinit var myListener: OnItemClickListener

    class ViewHolder(view: View, private var mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener {


        private val literatureItemRv = view.literature_item_Rv

        private val nameOfLiterature: TextView = view.nameOfLiterature
        private val author: TextView = view.author


        fun bind(itemData: LiteratureData, context: Context) {
            val typeLiterature = when (itemData.type) {
                "WORKBOOK" -> "Учебное пособие:"
                "BOOK" -> "Книга:"
                else -> {
                    ""
                }
            }
            val nameOfLiteratureText = "$typeLiterature ${'"'} ${itemData.title} ${'"'}"
            nameOfLiterature.text = nameOfLiteratureText
            author.text = itemData.authors


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
            literatureItemRv.background = drawable

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            mListener.setOnClickListener(adapterPosition)
        }
    }

    var dataList = ArrayList<LiteratureData>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(
            inflater.inflate(
                R.layout.literature_layout_item,
                parent,
                false
            ), myListener
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
        holder.itemView.setOnClickListener { myListener.setOnClickListener(holder.adapterPosition) }


    }


    override fun getItemCount(): Int = dataList.size
    interface OnItemClickListener {
        fun setOnClickListener(position: Int)
    }

    fun setOnItemClickListener(myListener: OnItemClickListener) {
        this.myListener = myListener
    }

}