package com.example.randomizadorprendas.API

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository para manejar las operaciones relacionadas con prendas
 */
class PrendasRepository {
    private val apiService = PrendasApiClient.service

    /**
     * Carga las prendas desde la API ASOS
     * Si la API no está disponible, retorna datos de ejemplo
     */
    suspend fun cargarPrendas(): PrendasResponse = withContext(Dispatchers.IO) {
        try {
            // Buscar productos por categorías
            val accesoriosCabeza = buscarPrendasPorCategoria("hat", "accesorio_cabeza")
            val poleras = buscarPrendasPorCategoria("t-shirt", "polera")
            val pantalones = buscarPrendasPorCategoria("trousers", "pantalon")
            val zapatos = buscarPrendasPorCategoria("shoes", "zapatos")

            PrendasResponse(
                accesoriosCabeza = accesoriosCabeza,
                poleras = poleras,
                pantalones = pantalones,
                zapatos = zapatos
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla la API, retornamos datos de ejemplo para demostración
            generarPrendasEjemplo()
        }
    }

    /**
     * Busca prendas por categoría usando la API de ASOS
     */
    private suspend fun buscarPrendasPorCategoria(query: String, tipo: String): List<Prenda> {
        return try {
            val response = apiService.searchProducts(
                query = query,
                limit = 10
            )
            response.products?.mapNotNull { product ->
                val imagenUrl = product.imageUrl 
                    ?: product.productImageUrls?.firstOrNull()
                    ?: product.additionalImageUrls?.firstOrNull()
                
                if (product.id != null && product.name != null && imagenUrl != null) {
                    Prenda(
                        id = product.id,
                        nombre = product.name,
                        tipo = tipo,
                        descripcion = product.description ?: product.brandName,
                        imagen = imagenUrl
                    )
                } else {
                    null
                }
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Genera prendas de ejemplo para demostración
     * Reemplazar con datos reales cuando la API esté disponible
     */
    private fun generarPrendasEjemplo(): PrendasResponse {
        return PrendasResponse(
            accesoriosCabeza = listOf(
                Prenda("1", "Gorro", "accesorio_cabeza", "Gorro de invierno", "https://images.asos-media.com/products/asos-design-beanie-in-black/201234123-1-black"),
                Prenda("2", "Bandana", "accesorio_cabeza", "Bandana de algodón", "https://images.asos-media.com/products/asos-design-bandana-in-navy/201234124-1-navy"),
                Prenda("3", "Sombrero", "accesorio_cabeza", "Sombrero de paja", "https://images.asos-media.com/products/asos-design-straw-hat/201234125-1-natural"),
                Prenda("4", "Gorra", "accesorio_cabeza", "Gorra de béisbol", "https://images.asos-media.com/products/asos-design-cap-in-black/201234126-1-black"),
                Prenda("5", "Boina", "accesorio_cabeza", "Boina elegante", "https://images.asos-media.com/products/asos-design-beret-in-black/201234127-1-black")
            ),
            poleras = listOf(
                Prenda("1", "Polera Blanca", "polera", "Polera básica blanca", "https://images.asos-media.com/products/asos-design-t-shirt-in-white/201234128-1-white"),
                Prenda("2", "Polera Negra", "polera", "Polera básica negra", "https://images.asos-media.com/products/asos-design-t-shirt-in-black/201234129-1-black"),
                Prenda("3", "Polera Azul", "polera", "Polera azul marino", "https://images.asos-media.com/products/asos-design-t-shirt-in-navy/201234130-1-navy"),
                Prenda("4", "Polera Rayada", "polera", "Polera a rayas", "https://images.asos-media.com/products/asos-design-striped-t-shirt/201234131-1-multi"),
                Prenda("5", "Polera Estampada", "polera", "Polera con estampado", "https://images.asos-media.com/products/asos-design-printed-t-shirt/201234132-1-multi")
            ),
            pantalones = listOf(
                Prenda("1", "Jeans", "pantalon", "Pantalón jeans clásico", "https://images.asos-media.com/products/asos-design-slim-jeans-in-blue/201234133-1-blue"),
                Prenda("2", "Chinos", "pantalon", "Pantalón chino beige", "https://images.asos-media.com/products/asos-design-chinos-in-beige/201234134-1-beige"),
                Prenda("3", "Deportivo", "pantalon", "Pantalón deportivo", "https://images.asos-media.com/products/asos-design-joggers-in-black/201234135-1-black"),
                Prenda("4", "Negro", "pantalon", "Pantalón negro formal", "https://images.asos-media.com/products/asos-design-trousers-in-black/201234136-1-black"),
                Prenda("5", "Cargo", "pantalon", "Pantalón cargo", "https://images.asos-media.com/products/asos-design-cargo-trousers-in-khaki/201234137-1-khaki")
            ),
            zapatos = listOf(
                Prenda("1", "Zapatillas", "zapatos", "Zapatillas deportivas", "https://images.asos-media.com/products/asos-design-sneakers-in-white/201234138-1-white"),
                Prenda("2", "Zapatos Formales", "zapatos", "Zapatos negros formales", "https://images.asos-media.com/products/asos-design-formal-shoes-in-black/201234139-1-black"),
                Prenda("3", "Botas", "zapatos", "Botas de cuero", "https://images.asos-media.com/products/asos-design-boots-in-brown/201234140-1-brown"),
                Prenda("4", "Mocasines", "zapatos", "Mocasines casuales", "https://images.asos-media.com/products/asos-design-loafers-in-brown/201234141-1-brown"),
                Prenda("5", "Sandalias", "zapatos", "Sandalias de verano", "https://images.asos-media.com/products/asos-design-sandals-in-black/201234142-1-black")
            )
        )
    }
}

