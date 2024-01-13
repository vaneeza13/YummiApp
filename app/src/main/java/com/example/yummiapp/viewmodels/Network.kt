package com.example.yummiapp.viewmodels

import okhttp3.OkHttpClient

object SingletonClient {
    val httpClient: OkHttpClient by lazy {
        OkHttpClient()
    }
}