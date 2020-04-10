package com.ppb.kwid.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ppb.kwid.Model.Notification.Notification
import com.ppb.kwid.Model.Notification.NotificationAdapter
import com.ppb.kwid.R

class NotificationActivity : AppCompatActivity() {

    private lateinit var btnBackNotification: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var listNotification: MutableList<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        btnBackNotification = findViewById(R.id.btn_back_notification)
        btnBackNotification.setOnClickListener {
            super.onBackPressed()
        }

        addListNotification()
        recyclerView = findViewById(R.id.recyclerview_notification)
        showRecyclerViewNotification()
    }

    private fun addListNotification() {
        val notificationType = resources.getStringArray(R.array.notification_type)
        val notificationHeader = resources.getStringArray(R.array.notification_header)
        val notificationBody = resources.getStringArray(R.array.notification_body)
        val notificationDate = resources.getStringArray(R.array.notification_date)

        for (position in notificationType.indices) {
            val notification = Notification(
                notificationType[position],
                notificationHeader[position],
                notificationBody[position],
                notificationDate[position]
            )

            listNotification.add(notification)
        }
    }

    private fun showRecyclerViewNotification() {
        val notificationAdapter = NotificationAdapter(listNotification)
        recyclerView.adapter = notificationAdapter
    }
}
