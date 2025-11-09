package com.example.randomizadorprendas

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.randomizadorprendas.API.Prenda
import com.example.randomizadorprendas.API.PrendasRepository
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import kotlin.random.Random
import java.io.File

class RandomizadorActivity : AppCompatActivity() {

    private lateinit var autoCompleteAccesorio: AutoCompleteTextView
    private lateinit var autoCompletePolera: AutoCompleteTextView
    private lateinit var autoCompletePantalon: AutoCompleteTextView
    private lateinit var autoCompleteZapatos: AutoCompleteTextView
    
    private lateinit var imageAccesorio: ImageView
    private lateinit var imagePolera: ImageView
    private lateinit var imagePantalon: ImageView
    private lateinit var imageZapatos: ImageView
    
    private lateinit var btnCargarPrendas: MaterialButton
    private lateinit var btnRandomizar: MaterialButton
    private lateinit var btnCameraAccesorio: MaterialButton
    private lateinit var btnCameraPolera: MaterialButton
    private lateinit var btnCameraPantalon: MaterialButton
    private lateinit var btnCameraZapatos: MaterialButton
    private lateinit var progressBar: ProgressBar

    private val prendasRepository = PrendasRepository()
    private var accesoriosCabeza: List<Prenda> = emptyList()
    private var poleras: List<Prenda> = emptyList()
    private var pantalones: List<Prenda> = emptyList()
    private var zapatos: List<Prenda> = emptyList()
    
    private var currentCategory: String = ""
    private var currentPhotoUri: Uri? = null

    // Activity Result Launcher para la cámara
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoUri != null) {
            loadImageFromCamera(currentCategory, currentPhotoUri!!)
        }
    }

    // Activity Result Launcher para permisos
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera(currentCategory)
        } else {
            Toast.makeText(
                this,
                "Se necesita permiso de cámara para agregar fotos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_randomizador)

        initViews()
        setupListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        autoCompleteAccesorio = findViewById(R.id.auto_complete_accesorio)
        autoCompletePolera = findViewById(R.id.auto_complete_polera)
        autoCompletePantalon = findViewById(R.id.auto_complete_pantalon)
        autoCompleteZapatos = findViewById(R.id.auto_complete_zapatos)
        
        imageAccesorio = findViewById(R.id.image_accesorio)
        imagePolera = findViewById(R.id.image_polera)
        imagePantalon = findViewById(R.id.image_pantalon)
        imageZapatos = findViewById(R.id.image_zapatos)
        
        btnCargarPrendas = findViewById(R.id.btn_cargar_prendas)
        btnRandomizar = findViewById(R.id.btn_randomizar)
        btnCameraAccesorio = findViewById(R.id.btn_camera_accesorio)
        btnCameraPolera = findViewById(R.id.btn_camera_polera)
        btnCameraPantalon = findViewById(R.id.btn_camera_pantalon)
        btnCameraZapatos = findViewById(R.id.btn_camera_zapatos)
        progressBar = findViewById(R.id.progress_bar)
    }

    private fun setupListeners() {
        btnCargarPrendas.setOnClickListener {
            cargarPrendasDesdeAPI()
        }

        btnRandomizar.setOnClickListener {
            randomizarOutfit()
        }
        
        btnCameraAccesorio.setOnClickListener {
            currentCategory = "accesorio"
            checkCameraPermission()
        }
        
        btnCameraPolera.setOnClickListener {
            currentCategory = "polera"
            checkCameraPermission()
        }
        
        btnCameraPantalon.setOnClickListener {
            currentCategory = "pantalon"
            checkCameraPermission()
        }
        
        btnCameraZapatos.setOnClickListener {
            currentCategory = "zapatos"
            checkCameraPermission()
        }
        
        // Listener para cuando se selecciona una prenda del dropdown
        autoCompleteAccesorio.setOnItemClickListener { _, _, position, _ ->
            val prenda = accesoriosCabeza[position]
            cargarImagenPrenda(prenda.imagen, imageAccesorio)
        }
        
        autoCompletePolera.setOnItemClickListener { _, _, position, _ ->
            val prenda = poleras[position]
            cargarImagenPrenda(prenda.imagen, imagePolera)
        }
        
        autoCompletePantalon.setOnItemClickListener { _, _, position, _ ->
            val prenda = pantalones[position]
            cargarImagenPrenda(prenda.imagen, imagePantalon)
        }
        
        autoCompleteZapatos.setOnItemClickListener { _, _, position, _ ->
            val prenda = zapatos[position]
            cargarImagenPrenda(prenda.imagen, imageZapatos)
        }
    }

    private fun cargarPrendasDesdeAPI() {
        progressBar.visibility = View.VISIBLE
        btnCargarPrendas.isEnabled = false

        lifecycleScope.launch {
            try {
                val respuesta = prendasRepository.cargarPrendas()

                accesoriosCabeza = respuesta.accesoriosCabeza
                poleras = respuesta.poleras
                pantalones = respuesta.pantalones
                zapatos = respuesta.zapatos

                // Configurar adapters para los AutoCompleteTextView
                configurarAdapters()

                Toast.makeText(
                    this@RandomizadorActivity,
                    getString(R.string.prendas_cargadas),
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@RandomizadorActivity,
                    "${getString(R.string.error_cargar)}: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressBar.visibility = View.GONE
                btnCargarPrendas.isEnabled = true
            }
        }
    }

    private fun configurarAdapters() {
        if (accesoriosCabeza.isNotEmpty()) {
            val adapterAccesorio = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                accesoriosCabeza.map { it.nombre }
            )
            autoCompleteAccesorio.setAdapter(adapterAccesorio)
        }

        if (poleras.isNotEmpty()) {
            val adapterPolera = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                poleras.map { it.nombre }
            )
            autoCompletePolera.setAdapter(adapterPolera)
        }

        if (pantalones.isNotEmpty()) {
            val adapterPantalon = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                pantalones.map { it.nombre }
            )
            autoCompletePantalon.setAdapter(adapterPantalon)
        }

        if (zapatos.isNotEmpty()) {
            val adapterZapatos = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                zapatos.map { it.nombre }
            )
            autoCompleteZapatos.setAdapter(adapterZapatos)
        }
    }

    private fun randomizarOutfit() {
        // Verificar que las prendas hayan sido cargadas
        if (accesoriosCabeza.isEmpty() || poleras.isEmpty() || pantalones.isEmpty() || zapatos.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.cargar_prendas_primero),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Randomizar cada categoría
        if (accesoriosCabeza.isNotEmpty()) {
            val prendaAleatoria = accesoriosCabeza[Random.nextInt(accesoriosCabeza.size)]
            autoCompleteAccesorio.setText(prendaAleatoria.nombre, false)
            cargarImagenPrenda(prendaAleatoria.imagen, imageAccesorio)
        }

        if (poleras.isNotEmpty()) {
            val prendaAleatoria = poleras[Random.nextInt(poleras.size)]
            autoCompletePolera.setText(prendaAleatoria.nombre, false)
            cargarImagenPrenda(prendaAleatoria.imagen, imagePolera)
        }

        if (pantalones.isNotEmpty()) {
            val prendaAleatoria = pantalones[Random.nextInt(pantalones.size)]
            autoCompletePantalon.setText(prendaAleatoria.nombre, false)
            cargarImagenPrenda(prendaAleatoria.imagen, imagePantalon)
        }

        if (zapatos.isNotEmpty()) {
            val prendaAleatoria = zapatos[Random.nextInt(zapatos.size)]
            autoCompleteZapatos.setText(prendaAleatoria.nombre, false)
            cargarImagenPrenda(prendaAleatoria.imagen, imageZapatos)
        }

        Toast.makeText(
            this,
            getString(R.string.outfit_randomizado),
            Toast.LENGTH_SHORT
        ).show()
    }
    
    private fun cargarImagenPrenda(url: String?, imageView: ImageView) {
        if (url != null && url.isNotEmpty()) {
            try {
                // Si es una URI local (file:// o content://), cargar directamente
                if (url.startsWith("content://") || url.startsWith("file://")) {
                    Glide.with(this)
                        .load(Uri.parse(url))
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .centerCrop()
                        .into(imageView)
                } else {
                    // Si es una URL HTTP/HTTPS, cargar desde internet
                    Glide.with(this)
                        .load(url)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .centerCrop()
                        .into(imageView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera(currentCategory)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                AlertDialog.Builder(this)
                    .setTitle("Permiso de Cámara")
                    .setMessage("La aplicación necesita acceso a la cámara para agregar fotos de prendas.")
                    .setPositiveButton("Aceptar") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    
    private fun openCamera(category: String) {
        val photoFile = File(getExternalFilesDir(null), "photo_${category}_${System.currentTimeMillis()}.jpg")
        val photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        currentPhotoUri = photoUri
        cameraLauncher.launch(photoUri)
    }
    
    private fun loadImageFromCamera(category: String, photoUri: Uri) {
        try {
            val imageView = when (category) {
                "accesorio" -> imageAccesorio
                "polera" -> imagePolera
                "pantalon" -> imagePantalon
                "zapatos" -> imageZapatos
                else -> return
            }
            
            // Cargar la imagen usando Glide
            Glide.with(this)
                .load(photoUri)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .centerCrop()
                .into(imageView)
            
            // Agregar la prenda a la lista correspondiente
            val nuevaPrenda = Prenda(
                id = "camera_${System.currentTimeMillis()}",
                nombre = "Prenda desde cámara",
                tipo = category,
                descripcion = "Agregada desde cámara",
                imagen = photoUri.toString()
            )
            
            when (category) {
                "accesorio" -> {
                    accesoriosCabeza = accesoriosCabeza + nuevaPrenda
                    configurarAdapters()
                }
                "polera" -> {
                    poleras = poleras + nuevaPrenda
                    configurarAdapters()
                }
                "pantalon" -> {
                    pantalones = pantalones + nuevaPrenda
                    configurarAdapters()
                }
                "zapatos" -> {
                    zapatos = zapatos + nuevaPrenda
                    configurarAdapters()
                }
            }
            
            Toast.makeText(
                this,
                "Foto agregada exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al cargar la imagen: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
