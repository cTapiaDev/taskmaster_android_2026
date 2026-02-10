package com.example.taskmaster

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.taskmaster.controller.viewmodel.TaskViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskViewModelTest {
    // Esta regla nos sirve para que LiveData funcione en los test
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Regla para que funcionen las corrutinas
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TaskViewModel
    private lateinit var fakeRepo: FakeTareaRepository

    @Before
    fun setup() {
        fakeRepo = FakeTareaRepository()
        viewModel = TaskViewModel(fakeRepo)
    }

    @Test
    fun `test inicial`() {
        Assert.assertEquals(0, viewModel.stressLevel.value)
    }

    @Test
    fun `validar rechazo en fallo`() {
        viewModel.agregarTarea("fallo")
        Assert.assertEquals("Error: fallo", viewModel.mensajeEstado.value)
    }

    @Test
    fun `validar guardado exitoso`() {
        viewModel.agregarTarea("Mensaje exitoso")
        Assert.assertEquals("¡Guardado con éxito!", viewModel.mensajeEstado.value)
    }

    @Test
    fun `modo turbo procesa todo rapido`() = runTest {
        viewModel.ejecutarModoTurbo()
        Assert.assertEquals(0, viewModel.stressLevel.value)
    }

    @Test
    fun `manejo de error de red`() = runTest {
        fakeRepo.debeFallar = true
        viewModel.agregarTarea("Tarea válida")
        Assert.assertTrue(viewModel.mensajeEstado.value?.contains("Error") == true)
        Assert.assertEquals(0, viewModel.stressLevel.value)
    }


}