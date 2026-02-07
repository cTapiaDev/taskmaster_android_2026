package com.example.taskmaster.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmaster.R
import com.example.taskmaster.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : Fragment(R.layout.fragment_task_detail) {

    private lateinit var binding: FragmentTaskDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskDetailBinding.bind(view)

        val descripcion = arguments?.getString(ARG_TASK_NAME) ?: "Sin nombre"
        binding.tvDetailTitle.text = descripcion

        binding.btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_TASK_NAME = "task_name"

        fun newInstance(nombre: String): TaskDetailFragment {
            val fragment = TaskDetailFragment()
            val args = Bundle()
            args.putString(ARG_TASK_NAME, nombre)
            fragment.arguments = args
            return fragment
        }
    }
}

