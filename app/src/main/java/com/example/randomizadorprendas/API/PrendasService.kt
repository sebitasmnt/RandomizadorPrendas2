package com.example.randomizadorprendas.API

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

/**
 * Interface para definir los endpoints de la API de ASOS
 */
interface PrendasService {
    /**
     * Obtiene detalles de un producto específico
     */
    @GET("products/detail")
    suspend fun getProductDetail(
        @Query("id") productId: String,
        @Query("lang") lang: String = "en-US",
        @Query("store") store: String = "US",
        @Query("currency") currency: String = "USD",
        @Query("sizeSchema") sizeSchema: String = "US"
    ): AsosProduct

    /**
     * Busca productos por categoría
     */
    @GET("products/v2/list")
    suspend fun searchProducts(
        @Query("store") store: String = "US",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 48,
        @Query("country") country: String = "US",
        @Query("sort") sort: String = "freshness",
        @Query("currency") currency: String = "USD",
        @Query("sizeSchema") sizeSchema: String = "US",
        @Query("lang") lang: String = "en-US",
        @Query("q") query: String? = null,
        @Query("categoryId") categoryId: String? = null
    ): AsosSearchResponse
}

