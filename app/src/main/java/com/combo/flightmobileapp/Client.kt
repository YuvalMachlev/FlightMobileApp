package com.combo.flightmobileapp

import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import java.net.HttpURLConnection
import java.net.URL
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Client : AppCompatActivity() {
    private lateinit var urlConn: URL
    private lateinit var con: HttpURLConnection
    var aileronValue = 0.0
    var elevatorValue = 0.0
    var throttleValue = 0f
    var rudderValue = 0f

    fun connect(url: String): Int {
        val temp: URL
        try {
            temp = URL(url)
            con = temp.openConnection() as HttpURLConnection
            urlConn = temp
            println("connected !")
        } catch (e: Exception) {
            //todo error message
            println("not connected !")
            return 0
        }
        return 1
    }

    fun sendData() {
        val json: String =
            "{\"aileron\": $aileronValue,\n \"rudder\": $rudderValue,\n \"elevator\": $elevatorValue,\n \"throttle\": $throttleValue\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.postData(rb).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("Failure sent !")
                /*if (!changeImage) {
                    return
                }*/
                /*Toast.makeText(applicationContext, t.message,
                    Toast.LENGTH_SHORT).show()*/
                return
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("response")
                /* if (!changeImage) {
                     return
                 }
                 if (response.code() != 200) {
                     val message = getResponseMessage(response)
                     showMessage(message)
                 }*/
            }
        })
    }

    fun changeImage(running: Boolean) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(this.urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        api.getImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (running) {
                    Toast.makeText(
                        applicationContext, t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!running) {
                    return
                }
                val iStream = response.body()?.byteStream()
                if (iStream == null) {
                    /* val message = getResponseMessage(response)
                     showMessage(message)*/
                    println("IMAGE ERROR - istream is null")
                }
                val bitmap = BitmapFactory.decodeStream(iStream)
                runOnUiThread {
                    val imageView = findViewById<ImageView>(R.id.simImg)
                    imageView.setImageBitmap(bitmap)
                }
            }
        })
    }
}