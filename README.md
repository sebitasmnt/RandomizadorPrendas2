# Randomizador de Prendas

Aplicación Android desarrollada en Kotlin que permite cargar prendas desde una API y randomizar outfits completos.

## Características

- **Pantalla de Bienvenida**: Interfaz profesional de bienvenida al usuario
- **Carga de Prendas desde API**: Integración con Retrofit para consumir APIs REST
- **Randomizador de Outfits**: Genera combinaciones aleatorias de prendas
- **Categorías de Prendas**:
  - Accesorios de cabeza (gorros, bandanas, etc.)
  - Poleras
  - Pantalones
  - Zapatos

## Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación principal
- **Retrofit**: Cliente HTTP para consumo de APIs REST
- **Moshi**: Convertidor JSON
- **Coroutines**: Para operaciones asíncronas
- **Material Design**: Componentes de UI modernos
- **ConstraintLayout**: Layouts flexibles y responsivos

## Estructura del Proyecto

```
RandomizadorPrendas/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/randomizadorprendas/
│   │   │   │   ├── API/
│   │   │   │   │   ├── Prenda.kt
│   │   │   │   │   ├── PrendasApiClient.kt
│   │   │   │   │   ├── PrendasService.kt
│   │   │   │   │   └── PrendasRepository.kt
│   │   │   │   ├── funciones/
│   │   │   │   │   └── ValidarConexionWAN.kt
│   │   │   │   ├── WelcomeActivity.kt
│   │   │   │   └── RandomizadorActivity.kt
│   │   │   └── res/
│   │   │       ├── layout/
│   │   │       ├── values/
│   │   │       └── ...
```

## Configuración de la API

La aplicación está configurada para usar una API REST. Por defecto, si la API no está disponible, la aplicación utilizará datos de ejemplo para demostración.

Para configurar tu propia API:

1. Edita `PrendasApiClient.kt` y cambia `BASE_URL` por la URL de tu API
2. Asegúrate de que tu API retorne un JSON con la siguiente estructura:

```json
{
  "accesoriosCabeza": [
    {"id": 1, "nombre": "Gorro", "tipo": "accesorio_cabeza", "descripcion": "..."}
  ],
  "poleras": [
    {"id": 1, "nombre": "Polera Blanca", "tipo": "polera", "descripcion": "..."}
  ],
  "pantalones": [
    {"id": 1, "nombre": "Jeans", "tipo": "pantalon", "descripcion": "..."}
  ],
  "zapatos": [
    {"id": 1, "nombre": "Zapatillas", "tipo": "zapatos", "descripcion": "..."}
  ]
}
```

## Uso

1. **Pantalla de Bienvenida**: Al abrir la aplicación, verás una pantalla de bienvenida
2. **Continuar**: Presiona el botón "Continuar" para ir al randomizador
3. **Cargar Prendas**: Presiona "Cargar Prendas desde API" para obtener las prendas disponibles
4. **Randomizar**: Presiona "Randomizar Outfit" para generar una combinación aleatoria
5. **Seleccionar Manualmente**: También puedes seleccionar prendas manualmente usando los campos desplegables

## Requisitos

- Android Studio Hedgehog o superior
- Android SDK 24 o superior
- Kotlin 1.9.24
- Gradle 8.10.2

## Instalación

1. Clona o descarga el proyecto
2. Abre el proyecto en Android Studio
3. Sincroniza el proyecto con Gradle
4. Ejecuta la aplicación en un emulador o dispositivo físico

## Permisos

La aplicación requiere los siguientes permisos:
- `INTERNET`: Para acceder a la API
- `ACCESS_NETWORK_STATE`: Para verificar el estado de la conexión

## Licencia

Este proyecto es de código abierto y está disponible para uso educativo.

# intentoapp
