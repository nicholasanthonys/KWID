package com.ppb.kwid.Model.Notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.R

class NotificationAdapter(private var notifications: MutableList<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationListHolder>() {

    inner class NotificationListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgNotification: ImageView = itemView.findViewById(R.id.img_notification)
        private val headerNotification: TextView =
            itemView.findViewById(R.id.txt_notification_header)
        private val dateNotification: TextView = itemView.findViewById(R.id.txt_notification_date)
        private val bodyNotification: TextView = itemView.findViewById(R.id.txt_notification_body)

        fun bind(notification: Notification) {
            if (notification.type.equals("system")) {
                imgNotification.setImageResource(R.drawable.icsharp_notifications)
            } else {
                imgNotification.setImageResource(R.drawable.icbaseline_lightbulb)
            }

            headerNotification.text = notification.header
            dateNotification.text = notification.date
            bodyNotification.text = notification.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_notification, parent, false)

        return NotificationListHolder(view)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationListHolder, position: Int) {
        holder.bind(notifications[position])
    }
}