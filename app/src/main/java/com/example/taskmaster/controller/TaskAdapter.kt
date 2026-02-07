package com.example.taskmaster.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.databinding.ItemTaskBinding
import com.example.taskmaster.model.Tarea

class TaskAdapter(
    private var tasks: List<Tarea>,
    private val onTaskClick: (Tarea) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task = tasks[position]
        holder.binding.tvTaskDescription.text = task.descripcion
        holder.itemView.setOnClickListener { onTaskClick(task) }
        holder.binding.btnDeleteTask.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateData(newTasks: List<Tarea>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }

}