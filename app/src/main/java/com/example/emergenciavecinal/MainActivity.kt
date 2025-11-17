package com.example.emergenciavecinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var btnEmergencyMedical: Button
    private lateinit var btnEmergencyCrime: Button
    private lateinit var btnAccessApp: Button
    private lateinit var tvUserName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        btnEmergencyMedical = findViewById(R.id.btnEmergencyMedical)
        btnEmergencyCrime = findViewById(R.id.btnEmergencyCrime)
        btnAccessApp = findViewById(R.id.btnAccessApp)
        tvUserName = findViewById(R.id.tvUserName)

        // Cargar datos del usuario
        loadUserData()

        // Botón Emergencia Médica (por ahora solo muestra mensaje)
        btnEmergencyMedical.setOnClickListener {
            Toast.makeText(this, "Emergencia Médica - Funcionalidad pendiente (Entrega 2)", Toast.LENGTH_SHORT).show()
        }

        // Botón Emergencia Delito (por ahora solo muestra mensaje)
        btnEmergencyCrime.setOnClickListener {
            Toast.makeText(this, "Emergencia Delito - Funcionalidad pendiente (Entrega 2)", Toast.LENGTH_SHORT).show()
        }

        // Botón Ingresar a la App (ir al perfil)
        btnAccessApp.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nombre = document.getString("nombre") ?: "Usuario"
                    tvUserName.text = nombre
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos del usuario cuando vuelve de otra pantalla
        loadUserData()
    }
}