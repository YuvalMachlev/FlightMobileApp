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
    var gotImage = false //boolean for connection check

    //if connected successfully - return 1
    fun connect(url: String): Int {
        val temp: URL
        try {
            temp = URL(url)
            con = temp.openConnection() as HttpURLConnection
            urlConn = temp
        } catch (e: Exception) {
            return 0
        }
        return 1
    }

    //send values to server, json file
    fun sendData(context: Context) {
        val json: String =
            "{\n\"aileron\": $aileronValue,\n \"rudder\": $rudderValue,\n \"elevator\": $elevatorValue,\n \"throttle\": $throttleValue\n}"
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
                //error messages
                Toast.makeText(
                    context, t.message,
                    Toast.LENGTH_SHORT
                ).show()

                Toast.makeText(
                    context, "NOTICE: You should press the back button to go back to main menu",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //error messages for error status code
                if (response.code() != 200) {
                    Toast.makeText(
                        context, response.message(),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response.code() != 400) {
                        Toast.makeText(
                            context,
                            "NOTICE: You should press the back button to go back to main menu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    //get image from server, and show it
    fun changeImage(running: Boolean, img: ImageView, context: Context) {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(this.urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        api.getImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (running) { //if failure while running, show error
                    Toast.makeText(
                        context, t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                //if screen isn't on the front, don't update image
                if (!running) {
                    return
                }
                val iStream = response.body()?.byteStream()
                //error message
                if (iStream == null) {
                    Toast.makeText(
                        context, "Input stream is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                //convert stream to image
                val bitmap = BitmapFactory.decodeStream(iStream)
                runOnUiThread {
                    img.setImageBitmap(bitmap)
                }
            }
        })
    }

    //get one image as connection condition
    fun getOneImage(): Int {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(this.urlConn.toString())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)
        api.getImage().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                gotImage = false
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                gotImage = true
            }

        })
        //just for case
        if (gotImage) {
            return 1
        }
        return 0
    }
}