package com.example.taskmaster.controller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaster.model.Tarea
import com.example.taskmaster.model.TareaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repositorio: TareaRepository = TareaRepository()
) : ViewModel() {

    private val _stressLevel = MutableLiveData<Int>(0)
    val stressLevel: LiveData<Int> get() = _stressLevel

    private val _tareas = MutableLiveData<List<Tarea>>()
    val tareas: LiveData<List<Tarea>> get() = _tareas

    private val _estaCargando = MutableLiveData<Boolean>()
    val estaCargando: LiveData<Boolean> get() = _estaCargando

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    private fun actualizarStress(delta: Int) {
        val valorActual = _stressLevel.value ?: 0
        _stressLevel.value = valorActual + delta
    }

    fun obtenerTareas() {
        _tareas.value = repositorio.obtenerTodas()
    }

    fun agregarTarea(texto: String) {
        if (texto.isBlank()) {
            _mensajeEstado.value = "Error: vacío"
            return
        }
        if (texto.contains("fallo")) {
            _mensajeEstado.value = "Error: fallo"
            return
        }

        viewModelScope.launch {
            actualizarStress(1)
            _estaCargando.value = true
            _mensajeEstado.value = "Subiendo a la nube..."

            val resultado = repositorio.agregarTareaEnNube(texto)

            resultado.onSuccess {
                _tareas.value = repositorio.obtenerTodas()
                _mensajeEstado.value = "¡Guardado con éxito!"
            }.onFailure {
                _mensajeEstado.value = "Error: ${it.message}"
            }
            _estaCargando.value = false
            actualizarStress(-1)
        }
    }

    fun ejecutarModoTurbo() {
        viewModelScope.launch {
            _estaCargando.value = true
            _mensajeEstado.value = "MODO TURBO ACTIVO..."

            /* val deferreds = listOf(
                async { repositorio.agregarTareaEnNube("Tarea Turbo A") },
                async { repositorio.agregarTareaEnNube("Tarea Turbo B") },
                async { repositorio.agregarTareaEnNube("Tarea Turbo C") }
            )
            deferreds.awaitAll() */

            val tareasMasivas = List(10) { indice ->
                async {
                    actualizarStress(1)
                    val res = repositorio.agregarTareaEnNube("Tarea Turbo #$indice")
                    actualizarStress(-1)
                    res
                }
            }

            tareasMasivas.awaitAll()
            _tareas.value = repositorio.obtenerTodas()
            _estaCargando.value = false
            _mensajeEstado.value = "Turbo finalizado con éxito"
        }
    }

    fun limpiarTareas() {
        repositorio.limpiarTodo()
        _tareas.value = repositorio.obtenerTodas()
    }

}