package com.example.yummiapp.viewmodel

import okhttp3.OkHttpClient

object SingletonClient {
    val httpClient: OkHttpClient by lazy {
        OkHttpClient()
    }
}