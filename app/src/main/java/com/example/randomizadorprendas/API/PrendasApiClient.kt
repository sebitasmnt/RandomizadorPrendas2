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
    
    // IMPORTANTE: Para usar la API, necesitas configurar tus propias credenciales
    // Opción 1: Usar variables de entorno o BuildConfig (recomendado)
    // Opción 2: Crear un archivo local.properties y agregar:
    // RAPIDAPI_KEY=tu_api_key_aqui
    // RAPIDAPI_HOST=asos2.p.rapidapi.com
    // Luego leerlo en tiempo de ejecución
    // 
    // Por ahora, la aplicación funcionará con datos de ejemplo si la API no está configurada
    private val RAPIDAPI_KEY: String? = null // Configurar tu API key aquí o usar BuildConfig
    private const val RAPIDAPI_HOST = "asos2.p.rapidapi.com"

    // Interceptor para agregar headers de RapidAPI
    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        // Solo agregar headers si la API key está configurada
        RAPIDAPI_KEY?.let {
            requestBuilder
                .header("x-rapidapi-key", it)
                .header("x-rapidapi-host", RAPIDAPI_HOST)
        }
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

