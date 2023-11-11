package edu.uw.ischool.yuhuiyao.arewethereyet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.app.AlarmManager
import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {

    private var isServiceRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = findViewById<EditText>(R.id.msg)
        val startButton = findViewById<Button>(R.id.startButton)
        val phoneNum = findViewById<EditText>(R.id.phoneNumber)
        val minute = findViewById<EditText>(R.id.minute)

//        val interval = minute.text.toString().toInt()
//        if(interval == 0) {
//            Toast.makeText(this@MainActivity, "Please enter a positive, non-zero integer", Toast.LENGTH_SHORT).show()
//        }

        // ENABLE START BUTTON ONLY IF USER ENTERS ALL THE EDIT TEXT
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val isValidMessage = message.text.isNotEmpty()
                val isValidPhoneNumber = phoneNum.text.isNotEmpty()
                val isValidInterval = minute.text.isNotEmpty() && minute.text.toString().toInt() > 0
                startButton.isEnabled = isValidMessage && isValidPhoneNumber && isValidInterval
            }
        }

        message.addTextChangedListener(textWatcher)
        phoneNum.addTextChangedListener(textWatcher)
        minute.addTextChangedListener(textWatcher)


        startButton.setOnClickListener {
            if (isServiceRunning) {
                stopMessageService()
            } else {
                val intervalText = minute.text.toString()
                if (intervalText.isNotEmpty() && intervalText.toInt() > 0) {
                    val interval = intervalText.toLong() * 60 * 1000
                    startMessageService(interval)
                }
            }
        }
    }
    private fun startMessageService(interval: Long) {
        val msg = findViewById<EditText>(R.id.msg)
        val num = findViewById<EditText>(R.id.phoneNumber)
        val message = msg.text.toString()
        val phoneNum = num.text.toString().toInt()

        val serviceIntent = Intent(this, MessageService::class.java)
        serviceIntent.putExtra("interval", interval)
        serviceIntent.putExtra("message", message)
        serviceIntent.putExtra("phoneNum", phoneNum)

        startService(serviceIntent)
        isServiceRunning = true

        val btnStartStop: Button = findViewById(R.id.startButton)
        btnStartStop.text = "Stop"
    }

    private fun stopMessageService() {
        val serviceIntent = Intent(this, MessageService::class.java)
        stopService(serviceIntent)
        isServiceRunning = false

        val btnStartStop: Button = findViewById(R.id.startButton)
        btnStartStop.text = "Start"

    }
}