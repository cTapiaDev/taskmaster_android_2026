package com.example.taskmaster.model

class TareaRepository {
    private val listaTareas = mutableListOf<Tarea>();

    fun agregarTarea(descripcion: String) {
        if (descripcion.isNotBlank()) {
            listaTareas.add(Tarea(descripcion))
        }
    }

    fun obtenerTodas(): List<Tarea> = listaTareas

    fun limpiarTodo() {
        listaTareas.clear()
    }

}