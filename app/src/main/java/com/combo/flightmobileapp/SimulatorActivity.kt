package com.combo.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs


class SimulatorActivity : AppCompatActivity() {
    private var client = Client()
    private var lastAileron = 0.0
    private var lastElevator = 0.0
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra("url")
        if (url != null) {
            val c = client.connect(url)
            //connection check
            if (c == 1) {
                setContentView(R.layout.activity_simulator)
                //init controls
                initRudder()
                initThrottle()
                initJoystick()
                onStart()
            } else { //connection failed
                Toast.makeText(
                    applicationContext, "Connection failed, please try again!",
                    Toast.LENGTH_SHORT
                ).show()
                //goes back to mainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("url", url)
                startActivity(intent)
            }
        }
    }


    private fun initJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            val aileronValue = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevatorValue = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100

            //if the difference is more than 1%
            if (abs(aileronValue - lastAileron) >= 0.02 || abs(elevatorValue - lastElevator) >= 0.02) {
                lastAileron = aileronValue
                lastElevator = elevatorValue
                client.aileronValue = aileronValue
                client.elevatorValue = elevatorValue
                //coroutine send data
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
            }
        }
    }

    private fun initThrottle() {
        val throttle = findViewById<SeekBar>(R.id.throttle)
        throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val barValue = (i / 100f) //value between 0-1
                // update server
                client.throttleValue = barValue
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })
    }

    private fun initRudder() {
        val rudder = findViewById<SeekBar>(R.id.rudder)
        rudder.progress = (rudder.max / 2) //default is 0
        rudder.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val barValue = ((i - 100) / 100f) // value between -1 to 1
                // update server
                client.rudderValue = barValue
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })
    }

    override fun onStart() {
        super.onStart()
        running = true
        getImage()
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

    override fun onResume() {
        super.onResume()
        running = true
    }

    override fun onStop() {
        super.onStop()
        running = false
    }

    private fun getImage() {
        val img = findViewById<ImageView>(R.id.simImg)
        CoroutineScope(IO).launch {
            while (running) { //while simulator is on screen
                client.changeImage(running, img, applicationContext)
                delay(300)
            }
        }
    }
}