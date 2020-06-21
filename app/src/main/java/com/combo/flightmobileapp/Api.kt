package com.combo.flightmobileapp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @GET("/api/command")
    fun getImage(): Call<ResponseBody>

    @POST("/api/command")
    fun postData(@Body rb: RequestBody): Call<ResponseBody>
}