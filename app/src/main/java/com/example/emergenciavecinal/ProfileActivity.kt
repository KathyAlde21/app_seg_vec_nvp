package com.example.emergenciavecinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etRut: TextInputEditText
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        etRut = findViewById(R.id.etRut)
        btnEdit = findViewById(R.id.btnEdit)
        btnSave = findViewById(R.id.btnSave)
        btnLogout = findViewById(R.id.btnLogout)

        // Cargar datos del usuario
        loadUserData()

        // Botón Editar
        btnEdit.setOnClickListener {
            toggleEditMode(true)
        }

        // Botón Guardar
        btnSave.setOnClickListener {
            saveUserData()
        }

        // Botón Cerrar Sesión
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("usuarios")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    etName.setText(document.getString("nombre"))
                    etEmail.setText(document.getString("email"))
                    etAddress.setText(document.getString("direccion"))
                    etPhone.setText(document.getString("telefono"))
                    etRut.setText(document.getString("rut"))
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleEditMode(enable: Boolean) {
        isEditMode = enable

        // Habilitar/deshabilitar campos (excepto email que no se puede cambiar)
        etName.isEnabled = enable
        etAddress.isEnabled = enable
        etPhone.isEnabled = enable
        etRut.isEnabled = enable

        // Mostrar/ocultar botones
        if (enable) {
            btnEdit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
        }
    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid ?: return

        val name = etName.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val rut = etRut.text.toString().trim()

        // Validaciones
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || rut.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        // Actualizar en Firestore
        val updates = hashMapOf<String, Any>(
            "nombre" to name,
            "direccion" to address,
            "telefono" to phone,
            "rut" to rut
        )

        firestore.collection("usuarios")
            .document(userId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                toggleEditMode(false)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity() // Cierra todas las activities
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onBackPressed() {
        if (isEditMode) {
            // Si está en modo edición, cancelar y volver a modo lectura
            toggleEditMode(false)
            loadUserData() // Recargar datos originales
        } else {
            // Si no está editando, volver a MainActivity
            super.onBackPressed()
        }
    }
}