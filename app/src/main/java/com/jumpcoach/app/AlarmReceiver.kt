package com.jumpcoach.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.core.app.NotificationCompat
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
        val pendingResult = goAsync()
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.KOREAN
                tts?.setSpeechRate(0.85f)
                tts?.speak("제자리 뛰기 하자!", TextToSpeech.QUEUE_FLUSH, null, "jump_alarm")
            }
            android.os.Handler(context.mainLooper).postDelayed({
                tts?.shutdown()
                pendingResult.finish()
            }, 5000)
        }
        AlarmScheduler.scheduleAll(context)
    }

    private fun showNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "jump_alarm_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(channelId, "제자리 뛰기 알림", NotificationManager.IMPORTANCE_HIGH)
            )
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("제자리 뛰기 하자!")
            .setContentText("다섯 번만 천천히 해요.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        manager.notify(2001, notification)
    }
}
