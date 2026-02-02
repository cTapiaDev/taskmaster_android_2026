package com.example.taskmaster.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskmaster.R
import com.example.taskmaster.databinding.ActivityMainBinding
import com.example.taskmaster.model.TareaRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repositorio = TareaRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
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
    }

    private fun actualizarInterfaz() {
        val tareas = repositorio.obtenerTodas()
        val listaFormateada = tareas.joinToString(separator = "\n") { "- ${it.descripcion}" }
        binding.tvLista.text = listaFormateada.ifEmpty { "Lista vacía" }
    }
}