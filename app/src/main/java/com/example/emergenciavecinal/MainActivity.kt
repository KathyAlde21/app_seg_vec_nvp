package com.example.emergenciavecinal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.emergenciavecinal.models.Alert
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var btnEmergencyMedical: LinearLayout
    private lateinit var btnEmergencyCrime: LinearLayout
    private lateinit var btnAccessApp: LinearLayout
    private lateinit var tvUserName: TextView

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var pendingAlertType: String? = null // Para saber qué alerta activar después del permiso

    // Datos del usuario
    private var userName = ""
    private var userAddress = ""
    private var userPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializar vistas
        btnEmergencyMedical = findViewById(R.id.btnEmergencyMedical)
        btnEmergencyCrime = findViewById(R.id.btnEmergencyCrime)
        btnAccessApp = findViewById(R.id.btnAccessApp)
        tvUserName = findViewById(R.id.tvUserName)

        // Cargar datos del usuario
        loadUserData()

        // Botón Emergencia Médica
        btnEmergencyMedical.setOnClickListener {
            showConfirmationDialog("medica")
        }

        // Botón Emergencia Delito
        btnEmergencyCrime.setOnClickListener {
            showConfirmationDialog("delito")
        }

        // Botón Ingresar a la App (mostrar menú)
        btnAccessApp.setOnClickListener {
            showAccessMenu()
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    userName = document.getString("nombre") ?: "Usuario"
                    userAddress = document.getString("direccion") ?: ""
                    userPhone = document.getString("telefono") ?: ""
                    tvUserName.text = userName
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showConfirmationDialog(tipoEmergencia: String) {
        val message = if (tipoEmergencia == "medica") {
            getString(R.string.confirm_medical_message)
        } else {
            getString(R.string.confirm_crime_message)
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_alert_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.confirm_yes)) { _, _ ->
                checkLocationPermissionAndSendAlert(tipoEmergencia)
            }
            .setNegativeButton(getString(R.string.confirm_no), null)
            .show()
    }

    private fun checkLocationPermissionAndSendAlert(tipoEmergencia: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permiso ya otorgado, enviar alerta
            sendAlert(tipoEmergencia)
        } else {
            // Solicitar permiso
            pendingAlertType = tipoEmergencia
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, enviar alerta pendiente
                pendingAlertType?.let { sendAlert(it) }
                pendingAlertType = null
            } else {
                // Permiso denegado
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_required),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun sendAlert(tipoEmergencia: String) {
        Toast.makeText(this, getString(R.string.getting_location), Toast.LENGTH_SHORT).show()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Crear alerta
                val alertId = UUID.randomUUID().toString()
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener

                val alert = Alert(
                    alertId = alertId,
                    userId = userId,
                    userName = userName,
                    userAddress = userAddress,
                    userPhone = userPhone,
                    tipoEmergencia = tipoEmergencia,
                    latitud = location.latitude,
                    longitud = location.longitude,
                    fechaHora = System.currentTimeMillis(),
                    estado = "activa"
                )

                // Guardar en Firestore
                firestore.collection("alertas")
                    .document(alertId)
                    .set(alert)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.alert_sent), Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "${getString(R.string.alert_error)}: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAccessMenu() {
        val options = arrayOf(
            getString(R.string.profile_title),
            getString(R.string.view_history)
        )

        AlertDialog.Builder(this)
            .setTitle("Opciones")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, ProfileActivity::class.java))
                    1 -> startActivity(Intent(this, AlertHistoryActivity::class.java))
                }
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }
}