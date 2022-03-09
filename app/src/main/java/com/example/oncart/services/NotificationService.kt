package com.example.oncart.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.oncart.R
import com.example.oncart.eventBus.MessageEvent
import com.example.oncart.eventBus.ProductEvent
import com.example.oncart.helper.Constants
import com.example.oncart.model.ProductItems
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.coroutines.CoroutineContext

class NotificationService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default+job)
    private var list = listOf<ProductItems>()
    private var id = 0
    private var toPost = true
    override fun onCreate() {
        super.onCreate()
        if(!(EventBus.getDefault().isRegistered(this)))EventBus.getDefault().register(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Subscribe
    fun onMessageEvent(event: MessageEvent) {
        when(event.getString()) {
            getString(R.string.list_of_product_items) -> {
                val ev = event as ProductEvent
                list = ev.getListOfProductItems()
                if(toPost) postNotifications()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun postNotifications() {
        scope.launch {
            list.forEach{
                createNotification(it)
            }
        }
        toPost = false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun createNotification(productItem: ProductItems) {
        setUpNotificationChannel()
        id = getNotificationId()
        val notificationLayout = RemoteViews(Constants.PACKAGE_NAME, R.layout.notification_layout)
        notificationLayout.setTextViewText(R.id.tvNamePush, productItem.id)
        notificationLayout.setTextViewText(R.id.tvPricePush, productItem.price)
        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.carts)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setCustomContentView(notificationLayout)
            .setGroup(Constants.SAME_GRP)
            .build()
        withContext(Dispatchers.Main) {
            Picasso.get().load(productItem.imageUrl).into(notificationLayout, R.id.ivPush, id, notification)
        }
        getNotificationManager().notify(id, notification)
    }

    private fun setUpNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    private fun getNotificationManager(): NotificationManager = run {
        this.getSystemService(NotificationManager::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        getNotificationManager().createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel(null)
        EventBus.getDefault().unregister(this)
    }

    private fun getNotificationId(): Int {
        return (1..10000).random()
    }

}