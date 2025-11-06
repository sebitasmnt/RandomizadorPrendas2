package com.example.randomizadorprendas.API

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object PrendasApiClient {
    // URL base de la API ASOS RapidAPI
    private const val BASE_URL = "https://asos2.p.rapidapi.com/"
    private const val RAPIDAPI_KEY = "7533c19f41msh7bff547e6cd4e3dp18a85cjsnb8b8a66c1dcb"
    private const val RAPIDAPI_HOST = "asos2.p.rapidapi.com"

    // Interceptor para agregar headers de RapidAPI
    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("x-rapidapi-key", RAPIDAPI_KEY)
            .header("x-rapidapi-host", RAPIDAPI_HOST)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val http = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(logging)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: PrendasService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PrendasService::class.java)
    }
}

