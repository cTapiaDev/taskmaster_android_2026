package com.example.taskmaster.controller

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmaster.R
import com.example.taskmaster.controller.viewmodel.TaskViewModel
import com.example.taskmaster.databinding.ActivityMainBinding
import com.example.taskmaster.model.TareaRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    // private val repositorio = TareaRepository()

    // private var miJobActual: Job? = null

    // private var corrutinasActivas = 0

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.obtenerTareas()

        // setupListener()
        /* binding.btnAgregar.setOnClickListener {
            val texto = binding.etTarea.text.toString();
            // guardarTareaProcesando(texto)
            guardarNormal(texto)
        }

        binding.btnTurbo.setOnClickListener {
            ejecutarModoTurbo()
        }

        binding.btnCancelar.setOnClickListener {
            if (miJobActual?.isActive == true) {
                miJobActual?.cancel()
                binding.tvEstado.text = "Operación cancelada por el usuario"
                binding.tvEstado.setTextColor(Color.parseColor("#757575"))
                binding.progressBar.visibility = View.GONE
                habilitarBotones(true)
            }
        }

        binding.btnLimpiar.setOnClickListener {
            repositorio.limpiarTodo()
            actualizarInterfaz()
        }

         */
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

    /* private fun guardarTareaProcesando(descripcion: String) {
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
    } */

    /* private fun actualizarMonitor(delta: Int) {
        corrutinasActivas += delta
        binding.tvActive.text = "Hilos activos: $corrutinasActivas"
        binding.stressBar.progress = corrutinasActivas

        val color = if (corrutinasActivas > 5) Color.RED else Color.parseColor("#00FF00")
        binding.stressBar.progressTintList = ColorStateList.valueOf(color)
        binding.tvActive.setTextColor(color)
    } */

    private fun guardarNormal(texto: String) {
        /* miJobActual = lifecycleScope.launch {
            try {
                habilitarBotones(false)
                binding.progressBar.visibility = View.VISIBLE
                binding.tvEstado.text = "Subiendo tarea..."
                binding.tvEstado.setTextColor(Color.BLACK)

                val resultado = repositorio.agregarTareaEnNube(texto)

                resultado.onSuccess {
                    binding.etTarea.text.clear()
                    binding.tvEstado.text = "¡Guardado con éxito!"
                    actualizarInterfaz()
                }.onFailure { error ->
                    binding.tvEstado.text = "Error: ${error.message}"
                    binding.tvEstado.setTextColor(Color.RED)
                }
            } finally {
                binding.progressBar.visibility = View.GONE
                habilitarBotones(true)
            }
        } */

        /* miJobActual = lifecycleScope.launch {
            try {
                actualizarMonitor(1)
                binding.progressBar.visibility = View.VISIBLE
                binding.tvEstado.text = "Intentando subir..."

                withTimeout(5000) {
                    repositorio.agregarTareaEnNube(texto)
                }

                binding.tvEstado.text = "¡Subido a tiempo!"
                actualizarInterfaz()
            } catch (e: TimeoutCancellationException) {
                binding.tvEstado.text = "El servidor es muy lento..."
                binding.tvEstado.setTextColor(Color.parseColor("#E65100"))
                throw e
            } catch (e: Exception) {
                binding.tvEstado.text = "Error: ${e.message}"
            } finally {
                actualizarMonitor(-1)
                binding.progressBar.visibility = View.GONE
            }
        } */
    }

    private fun ejecutarModoTurbo() {
        /* miJobActual = lifecycleScope.launch {
            val tiempoInicio = System.currentTimeMillis()
            habilitarBotones(false)
            binding.progressBar.visibility = View.VISIBLE
            binding.tvEstado.setTextColor(Color.BLUE)

            binding.tvEstado.text = "MODO TURBO: Enviando 10 tareas al mismo tiempo."

            try {
                /* val deferred1 = async { repositorio.agregarTareaEnNube("Tarea Turbo A") }
                val deferred2 = async { repositorio.agregarTareaEnNube("Tarea Turbo B") }
                val deferred3 = async { repositorio.agregarTareaEnNube("Tarea Turbo C") }
                */

                // val resultados = awaitAll(deferred1, deferred2, deferred3)

                val rafaga = List(10) { indice ->
                    async {
                        actualizarMonitor(1)
                        val res = repositorio.agregarTareaEnNube("Tarea Masiva #$indice")
                        actualizarMonitor(-1)
                        res
                    }
                }

                binding.tvEstado.text = "Esperando que el servidor responda a las 10 tareas..."
                awaitAll(*rafaga.toTypedArray())

                /* val tiempoTotal = (System.currentTimeMillis() - tiempoInicio) / 1000
                binding.tvEstado.text = "3 tareas ejecutadas en $tiempoTotal seg" */
                actualizarInterfaz()
            } catch (e: CancellationException) {
                corrutinasActivas = 0
                actualizarMonitor(0)
                throw e
            } finally {
                binding.progressBar.visibility = View.GONE
                habilitarBotones(true)
            }
        } */
    }

    /* private fun habilitarBotones(estado: Boolean) {
        binding.btnAgregar.isEnabled = estado
        binding.btnTurbo.isEnabled = estado
        binding.etTarea.isEnabled = estado
    } */

    /* private fun guardarConManejoDeErrores(texto: String) {
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
    } */

    private fun mostrarDetalleTarea(nombreTarea: String) {
        val fragment = TaskDetailFragment.newInstance(nombreTarea)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            // tasks = repositorio.obtenerTodas(),
            tasks = emptyList(),
            onTaskClick = { tarea -> mostrarDetalleTarea(tarea.descripcion) },
            onDeleteClick = { posicion ->
                Toast.makeText(this, "Eliminar tarea posición $posicion", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvTareas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }
    }

    private fun setupObservers() {
        viewModel.tareas.observe(this) { lista ->
            taskAdapter.updateData(lista)
        }

        viewModel.estaCargando.observe(this) { cargando ->
            binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnAgregar.isEnabled = !cargando
            binding.btnTurbo.isEnabled = !cargando
        }

        viewModel.mensajeEstado.observe(this) { mensaje ->
            binding.tvEstado.text = mensaje
        }

        viewModel.stressLevel.observe(this) { nivel ->
            binding.tvActive.text = "Hilos activos: $nivel"
            binding.stressBar.progress = nivel

            val color = if (nivel > 5) {
                Color.RED
            } else {
                Color.GREEN
            }

            binding.stressBar.progressTintList = ColorStateList.valueOf(color)
            binding.tvActive.setTextColor(color)
        }
    }

    private fun setupListeners() {
        binding.btnAgregar.setOnClickListener {
            val texto = binding.etTarea.text.toString()
            viewModel.agregarTarea(texto)
            binding.etTarea.text.clear()
        }

        binding.btnTurbo.setOnClickListener {
            viewModel.ejecutarModoTurbo()
        }

        binding.btnLimpiar.setOnClickListener {
            viewModel.limpiarTareas()
        }
    }

    /* private fun actualizarInterfaz() {
        /* val tareas = repositorio.obtenerTodas()
        val listaFormateada = tareas.joinToString(separator = "\n") { "- ${it.descripcion}" }
        binding.tvLista.text = listaFormateada.ifEmpty { "Lista vacía" } */
        taskAdapter.updateData(repositorio.obtenerTodas())
    } */
}