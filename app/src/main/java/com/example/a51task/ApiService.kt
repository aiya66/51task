package com.example.a51task

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType

object ApiService {
    private val client = OkHttpClient()
    private const val BASE_URL = "https://www.wanandroid.com"

    /**
     * 登录请求
     */
    fun login(username: String, password: String, callback: Callback) {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder()
            .url("$BASE_URL/user/login")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    /**
     * 注册请求
     */
    fun register(username: String, password: String, repassword: String, callback: Callback) {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .add("repassword", repassword)
            .build()
        val request = Request.Builder()
            .url("$BASE_URL/user/register")
            .post(formBody)
            .build()
        client.newCall(request).enqueue(callback)
    }
}