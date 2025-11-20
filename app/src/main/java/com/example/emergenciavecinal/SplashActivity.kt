package com.example.emergenciavecinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnTestMode: Button
    private var shouldAutoNavigate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        btnTestMode = findViewById(R.id.btnTestMode)

        // Botón Modo Prueba
        btnTestMode.setOnClickListener {
            shouldAutoNavigate = false
            // Ir directo a MainActivity sin autenticación
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Esperar 2 segundos y verificar si hay usuario logueado
        Handler(Looper.getMainLooper()).postDelayed({
            if (shouldAutoNavigate) {
                checkUserStatus()
            }
        }, 2000)
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Usuario ya logueado, ir a MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // No hay usuario, ir a Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}