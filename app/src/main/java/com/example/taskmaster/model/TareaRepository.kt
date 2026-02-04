package com.example.taskmaster.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TareaRepository {
    private val listaTareas = mutableListOf<Tarea>();

    /* fun agregarTarea(descripcion: String) {
        if (descripcion.isNotBlank()) {
            listaTareas.add(Tarea(descripcion))
        }
    } */

    /* suspend fun agregarTareaConRetraso(descripcion: String): Boolean {
        return withContext(Dispatchers.IO) {
            delay(3000)
            if (descripcion.isNotBlank()) {
                listaTareas.add(Tarea(descripcion))
                true
            } else {
                false
            }
        }
    } */

    suspend fun agregarTareaEnNube(descripcion: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val delayAleatorio = (2000..7000).random().toLong()
            delay(delayAleatorio)

            val errorAleatorio = (1..5).random()

            if (errorAleatorio == 1) {
                Result.failure(Exception("Servidor ocupado"))
            } else if (descripcion.isBlank()) {
                Result.failure(Exception("Texto vacío"))
            } else {
                listaTareas.add(Tarea(descripcion))
                Result.success(true)
            }
        }
    }

    fun obtenerTodas(): List<Tarea> = listaTareas.toList()

    fun limpiarTodo() {
        listaTareas.clear()
    }

}