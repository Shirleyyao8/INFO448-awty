package edu.uw.ischool.yuhuiyao.arewethereyet

// MessageService.kt
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask
import android.util.Log
import android.telephony.SmsManager


class MessageService : Service() {

    private var timer: Timer? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var serviceIntent: Intent? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MessageService", "onStartCommand called")
        serviceIntent = intent
        val interval = intent?.getLongExtra("interval", 0) ?: 0
        startSendingMessages(interval)
        return START_STICKY
    }


    private fun startSendingMessages(interval: Long) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                showMessage()
            }
        }, 0, interval)
    }

    private fun showMessage() {
        Log.d("MessageService", "showMessage called")
        handler.post {
            val phoneNumber = serviceIntent?.getIntExtra("phoneNum", 0)?.toString() ?: ""
            val message = serviceIntent?.getStringExtra("message") ?: ""

            Log.d("MessageService", "Phone number: $phoneNumber, Message: $message")

            // Use SmsManager to send SMS
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
