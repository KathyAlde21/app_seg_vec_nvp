package com.example.emergenciavecinal.models

data class Alert(
    val alertId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userAddress: String = "",
    val userPhone: String = "",
    val tipoEmergencia: String = "", // "medica" o "delito"
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val fechaHora: Long = System.currentTimeMillis(),
    val estado: String = "activa" // "activa", "resuelta", "falsa"
)