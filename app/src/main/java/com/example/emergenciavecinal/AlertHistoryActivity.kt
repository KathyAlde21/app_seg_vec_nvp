package com.example.emergenciavecinal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.emergenciavecinal.models.Alert
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class AlertHistoryActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoAlerts: TextView
    private lateinit var btnBack: ImageView
    private val alertsList = mutableListOf<Alert>()
    private lateinit var adapter: AlertsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_history)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewAlerts)
        tvNoAlerts = findViewById(R.id.tvNoAlerts)
        btnBack = findViewById(R.id.btnBack)

        // Configurar RecyclerView
        adapter = AlertsAdapter(alertsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Botón volver
        btnBack.setOnClickListener {
            finish()
        }

        // Cargar alertas
        loadAlerts()
    }

    private fun loadAlerts() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("alertas")
            .whereEqualTo("userId", userId)
            .orderBy("fechaHora", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                alertsList.clear()
                for (document in documents) {
                    val alert = document.toObject(Alert::class.java)
                    alertsList.add(alert)
                }

                if (alertsList.isEmpty()) {
                    tvNoAlerts.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvNoAlerts.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar alertas: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Adapter para RecyclerView
    inner class AlertsAdapter(private val alerts: List<Alert>) :
        RecyclerView.Adapter<AlertsAdapter.AlertViewHolder>() {

        inner class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val colorIndicator: View = view.findViewById(R.id.colorIndicator)
            val tvAlertType: TextView = view.findViewById(R.id.tvAlertType)
            val tvAlertDateTime: TextView = view.findViewById(R.id.tvAlertDateTime)
            val tvAlertLocation: TextView = view.findViewById(R.id.tvAlertLocation)
            val tvAlertStatus: TextView = view.findViewById(R.id.tvAlertStatus)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alert, parent, false)
            return AlertViewHolder(view)
        }

        override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
            val alert = alerts[position]

            // Tipo de emergencia y color
            if (alert.tipoEmergencia == "medica") {
                holder.tvAlertType.text = getString(R.string.alert_medical)
                holder.colorIndicator.setBackgroundColor(getColor(R.color.emergency_medical))
            } else {
                holder.tvAlertType.text = getString(R.string.alert_crime)
                holder.colorIndicator.setBackgroundColor(getColor(R.color.emergency_crime))
            }

            // Fecha y hora
            val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
            holder.tvAlertDateTime.text = dateFormat.format(Date(alert.fechaHora))

            // Ubicación
            holder.tvAlertLocation.text = "Lat: ${String.format("%.4f", alert.latitud)}, " +
                    "Long: ${String.format("%.4f", alert.longitud)}"

            // Estado
            when (alert.estado) {
                "activa" -> {
                    holder.tvAlertStatus.text = getString(R.string.alert_status_active)
                    holder.tvAlertStatus.setBackgroundColor(getColor(R.color.emergency_access))
                }
                "resuelta" -> {
                    holder.tvAlertStatus.text = getString(R.string.alert_status_resolved)
                    holder.tvAlertStatus.setBackgroundColor(Color.GRAY)
                }
                else -> {
                    holder.tvAlertStatus.text = getString(R.string.alert_status_false)
                    holder.tvAlertStatus.setBackgroundColor(Color.DKGRAY)
                }
            }
        }

        override fun getItemCount(): Int = alerts.size
    }
}
