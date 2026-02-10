package com.example.taskmaster

import com.example.taskmaster.model.Tarea
import com.example.taskmaster.model.TareaRepository

class FakeTareaRepository: TareaRepository() {
    private val datosFalsos = mutableListOf<Tarea>()
    var debeFallar = false

    override suspend fun agregarTareaEnNube(descripcion: String): Result<Boolean> {
        if (debeFallar) return Result.failure(Exception("Error de prueba"))
        datosFalsos.add(Tarea(descripcion))
        return Result.success(true)
    }

    override fun obtenerTodas(): List<Tarea> = datosFalsos
}