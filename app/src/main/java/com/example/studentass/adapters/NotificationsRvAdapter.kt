package com.example.studentass.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentass.R
import com.example.studentass.models.NotificationsItem
import kotlinx.android.synthetic.main.notifications_rv_item.view.*

class NotificationsRvAdapter (private val context : Context) : RecyclerView.Adapter<NotificationsRvAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val mainLl = view.mainLl
        val typeIv = view.typeIv
        val titleTv = view.titleTv
        val contentTv = view.contentTv
        val actionsBn = view.actionsBn

        fun bind(itemData: NotificationsItem, context: Context) {
            val drawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.notification_background)!!)
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, when (itemData.new) {
                1 -> R.color.colorNotificationNewBackground
                else -> R.color.colorNotificationBackground
            }))
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
            mainLl.background = drawable

            typeIv.setImageDrawable(ContextCompat.getDrawable(context, when (itemData.type) {
                0 -> R.drawable.ic_notifications_message
                else -> R.drawable.ic_notifications_schedule_changed
            }))

            titleTv.text = itemData.title

            contentTv.text = itemData.content
        }
    }

    var dataList = ArrayList<NotificationsItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsRvAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return NotificationsRvAdapter.ViewHolder(inflater.inflate(R.layout.notifications_rv_item, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationsRvAdapter.ViewHolder, position: Int) {
        val itemData = dataList[position]
        holder.bind(itemData, context)
    }

    override fun getItemCount(): Int = dataList.size
}