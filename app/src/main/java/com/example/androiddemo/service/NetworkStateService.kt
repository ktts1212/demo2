package com.example.androiddemo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.R
import com.example.androiddemo.R.drawable.warning

class NetworkStateService : Service() {

    var connectionmgr: ConnectivityManager? = null

    var receiver: BroadcastReceiver? = null

    var info: NetworkInfo? = null

    private lateinit var channel: NotificationChannel

    private lateinit var notificationManager: NotificationManager

    private lateinit var notification: Notification

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        receiver = NetworkConnectChangedReceiver()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class NetworkConnectChangedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                Log.d("AAAAAA", "网络状态发生变化")
                connectionmgr =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                info = connectionmgr?.activeNetworkInfo
                if (info != null && info!!.isAvailable()) {
                    val name = info?.typeName
                    Log.d("AAAAAA", "当前网络名称" + name)
                    stopForeground(STOP_FOREGROUND_REMOVE)
                } else {
                    Log.d("AAAAAA", "没有网络")
                    notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        channel = NotificationChannel(
                            "channel_id",
                            "channel",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    }
                    notificationManager.createNotificationChannel(channel)
                    notification = Notification.Builder(MainApplicaiton.context, "channel_id")
                        .setContentTitle("网络无法连接")
                        .setContentText("当前手机没有网络，请检查网络的连通性")
                        .setSmallIcon(warning)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, warning))
                        .build()
                    startForeground(1, notification)
                }
            }
        }
    }
}
