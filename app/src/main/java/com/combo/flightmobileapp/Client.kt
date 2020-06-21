package com.combo.flightmobileapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_simulator.*
import kotlinx.coroutines.delay
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
    var gotImage = false
    //lateinit var bitmap: Bitmap

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

    fun sendData(context: Context) {
        val json: String =
            "{\n\"aileron\": $aileronValue,\n \"rudder\": $rudderValue,\n \"elevator\": $elevatorValue,\n \"throttle\": $throttleValue\n}"
        //println(json)
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
                Toast.makeText(
                    context, t.message,
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() != 200) {
                    Toast.makeText(
                        context, response.message(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    fun changeImage(running: Boolean, img: ImageView, context: Context) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(this.urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        api.getImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (running) {
                    Toast.makeText(
                        context, t.message,
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
                    Toast.makeText(
                        context, "Input stream is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val bitmap = BitmapFactory.decodeStream(iStream)
                runOnUiThread {
                    img.setImageBitmap(bitmap)
                }
            }
        })
    }

    fun getOneImage(): Int {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(this.urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        api.getImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                gotImage = false
                println("falsseeeeeeeeeee")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                gotImage = true
                println("the value of gotImage is: TRUE")
                /*val intent = Intent(context, SimulatorActivity::class.java)
                intent.putExtra("url", urlConn)
                startActivity(intent)*/
            }

        })
        if (gotImage) {
            return 1
        }
        return 0
    }
}