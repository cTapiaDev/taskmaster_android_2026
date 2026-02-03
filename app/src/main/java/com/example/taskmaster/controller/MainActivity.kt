package com.example.taskmaster.controller

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.taskmaster.R
import com.example.taskmaster.databinding.ActivityMainBinding
import com.example.taskmaster.model.TareaRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repositorio = TareaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setupListener()
        binding.btnAgregar.setOnClickListener {
            val texto = binding.etTarea.text.toString();
            // guardarTareaProcesando(texto)
            guardarConManejoDeErrores(texto)
        }

        binding.btnLimpiar.setOnClickListener {
            repositorio.limpiarTodo()
            actualizarInterfaz()
        }
    }

    /* private fun setupListener() {
        binding.btnAgregar.setOnClickListener {
            val texto = binding.etTarea.text.toString()
            repositorio.agregarTarea(texto)
            binding.etTarea.text.clear()
            actualizarInterfaz()
        }

        binding.btnLimpiar.setOnClickListener {
            repositorio.limpiarTodo()
            actualizarInterfaz()
        }
    } */

    private fun guardarTareaProcesando(descripcion: String) {
        // Corrutina
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnAgregar.isEnabled = false
            binding.etTarea.isEnabled = false

            val exito = repositorio.agregarTareaConRetraso(descripcion)

            binding.progressBar.visibility = View.GONE
            binding.btnAgregar.isEnabled = true
            binding.etTarea.isEnabled = true

            if (exito) {
                binding.etTarea.text.clear()
                actualizarInterfaz()
            } else {
                Toast.makeText(this@MainActivity, "Error: Tarea vacía", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarConManejoDeErrores(texto: String) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.tvEstado.text = "Conectando con el servidor..."

            val resultado = repositorio.agregarTareaEnNube(texto)

            binding.progressBar.visibility = View.GONE

            resultado.onSuccess {
                binding.etTarea.text.clear()
                binding.tvEstado.text = "Guardado con éxito"
                actualizarInterfaz()
            }.onFailure { error ->
                binding.tvEstado.text = "Falló: ${error.message}"
                binding.tvEstado.setTextColor(Color.RED)
            }
        }
    }

    private fun actualizarInterfaz() {
        val tareas = repositorio.obtenerTodas()
        val listaFormateada = tareas.joinToString(separator = "\n") { "- ${it.descripcion}" }
        binding.tvLista.text = listaFormateada.ifEmpty { "Lista vacía" }
    }
}