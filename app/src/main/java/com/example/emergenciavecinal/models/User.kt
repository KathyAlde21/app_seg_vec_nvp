package com.example.emergenciavecinal.models

data class User(
    val userId: String = "",
    val nombre: String = "",
    val email: String = "",
    val direccion: String = "",
    val telefono: String = "",
    val rut: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)