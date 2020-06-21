package com.combo.flightmobileapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
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
            if (c == 1) {
                setContentView(R.layout.activity_simulator)
                initRudder()
                initThrottle()
                initJoystick()
                onStart()
            } else {
                /*     val intent = Intent(this, MainActivity::class.java)
                     intent.putExtra("url", url)
                     startActivity(intent)*/
            }
        }


    }


    private fun initJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            // get values
            val aileronValue = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevatorValue = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100

            if (abs(aileronValue - lastAileron) >= 0.02 || abs(elevatorValue - lastElevator) >= 0.02) {
                lastAileron = aileronValue
                lastElevator = elevatorValue
                client.aileronValue = aileronValue
                client.elevatorValue = elevatorValue
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
                //client.sendData()
             /*   println("aileron:")
                println(aileronValue)
                println("------------")
                println("elevator:")
                println(elevatorValue)*/
            }
        }

    }

    private fun initThrottle() {

        val throttle = findViewById<SeekBar>(R.id.throttle)

        throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val barValue = (i / 100f)
                // update server
                client.throttleValue = barValue
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
                //client.sendData();
                //println(barValue)
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
        rudder.setProgress(rudder.max / 2)

        rudder.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val barValue = ((i - 100) / 100f)
                // update server
                client.rudderValue = barValue
                CoroutineScope(IO).launch { client.sendData(applicationContext) }
                //client.sendData()
                //println(barValue)
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
        //todo check if condition needed
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
        //running = true
        val img = findViewById<ImageView>(R.id.simImg)
        CoroutineScope(IO).launch {
            while (running) {
                client.changeImage(running, img, applicationContext)
                delay(300)
            }

        }
    }


}