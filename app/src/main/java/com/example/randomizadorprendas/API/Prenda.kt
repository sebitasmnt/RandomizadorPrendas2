package com.example.randomizadorprendas.API

/**
 * Data class para representar una prenda genérica
 */
data class Prenda(
    val id: String,
    val nombre: String,
    val tipo: String, // "accesorio_cabeza", "polera", "pantalon", "zapatos"
    val descripcion: String? = null,
    val imagen: String? = null
)

/**
 * Data class para la respuesta de la API ASOS - Producto individual
 */
data class AsosProduct(
    val id: String? = null,
    val name: String? = null,
    val price: AsosPrice? = null,
    val brandName: String? = null,
    val imageUrl: String? = null,
    val productImageUrls: List<String>? = null,
    val additionalImageUrls: List<String>? = null,
    val description: String? = null,
    val categoryName: String? = null
)

data class AsosPrice(
    val current: AsosPriceValue? = null
)

data class AsosPriceValue(
    val text: String? = null,
    val value: Double? = null
)

/**
 * Data class para la respuesta de búsqueda de ASOS
 */
data class AsosSearchResponse(
    val products: List<AsosProduct>? = null
)

/**
 * Data class para la respuesta de la API
 */
data class PrendasResponse(
    val accesoriosCabeza: List<Prenda>,
    val poleras: List<Prenda>,
    val pantalones: List<Prenda>,
    val zapatos: List<Prenda>
)

