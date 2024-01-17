package com.example.yummiapp.viewmodel

import okhttp3.OkHttpClient

//a singleton instance of OkHttpClient to reuse for all calls
object SingletonClient {
    val httpClient: OkHttpClient by lazy {
        OkHttpClient()
    }
}