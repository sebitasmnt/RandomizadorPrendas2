package com.example.randomizadorprendas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.randomizadorprendas.funciones.ValidarConexionWAN

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        // Verificar conexión a internet
        if (ValidarConexionWAN.isOnline(this)) {
            println("Conectado a internet")
        } else {
            println("Sin conexión a internet")
        }

        // Inicializar botón de continuar
        val btnContinuar: Button = findViewById(R.id.btn_continuar)

        btnContinuar.setOnClickListener {
            // Crear intent para ir a la actividad del randomizador
            val intent = Intent(this, RandomizadorActivity::class.java)
            startActivity(intent)
            // Animación de transición
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

