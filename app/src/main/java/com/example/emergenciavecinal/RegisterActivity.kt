package com.example.emergenciavecinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.emergenciavecinal.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etRut: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        etRut = findViewById(R.id.etRut)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        // Botón de registro
        btnRegister.setOnClickListener {
            registerUser()
        }

        // Link a login
        tvLogin.setOnClickListener {
            finish() // Volver a LoginActivity
        }
    }

    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val rut = etRut.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validaciones
        if (name.isEmpty() || email.isEmpty() || address.isEmpty() ||
            phone.isEmpty() || rut.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, getString(R.string.error_short_password), Toast.LENGTH_SHORT).show()
            return
        }

        // Crear usuario en Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Usuario creado, ahora guardar datos en Firestore
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    saveUserToFirestore(userId, name, email, address, phone, rut)
                } else {
                    Toast.makeText(
                        this,
                        "${getString(R.string.error_register)}: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun saveUserToFirestore(
        userId: String,
        name: String,
        email: String,
        address: String,
        phone: String,
        rut: String
    ) {
        val user = User(
            userId = userId,
            nombre = name,
            email = email,
            direccion = address,
            telefono = phone,
            rut = rut,
            fechaRegistro = System.currentTimeMillis()
        )

        firestore.collection("usuarios")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}