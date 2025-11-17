package com.example.emergenciavecinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        // Esperar 2 segundos y verificar si hay usuario logueado
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
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