package com.art.realtimeapp.recyclerView

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.art.realtimeapp.Actions.Actions
import com.art.realtimeapp.R
import com.art.realtimeapp.Todo
import com.art.realtimeapp.databinding.DialogDeleteTaskBinding
import com.art.realtimeapp.databinding.ItemTodoBinding

class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTodoBinding.bind(view)

    private fun showDialogDelete(context: Context, onDeleteConfirmed: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        val dialogBinding = DialogDeleteTaskBinding.inflate(LayoutInflater.from(context))
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.show()

        dialogBinding.btnDeleteTask.setOnClickListener {
            onDeleteConfirmed()
            dialog.dismiss()
        }

        dialogBinding.btnCancelTask.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun bind(todoTask: Pair<String, Todo>, onItemSelected: (Actions, String) -> Unit) {
        binding.tvTitle.text = todoTask.second.title
        binding.tvDescription.text = todoTask.second.description

        binding.btnDelete.setOnClickListener {
            showDialogDelete(binding.root.context) {
                onItemSelected(Actions.DELETE, todoTask.first)
            }
        }

        binding.btnDeleteSpace.setOnClickListener {
            showDialogDelete(binding.root.context) {
                onItemSelected(Actions.DELETE, todoTask.first)
            }
        }

        binding.btnDeleteSpace.visibility = if (todoTask.second.done == true) {
            View.VISIBLE
        } else {
            View.GONE
        }

        val textColorId = if (todoTask.second.done == true) {
            R.color.gray
        } else {
            R.color.black
        }

        val textColor = ContextCompat.getColor(binding.tvTitle.context, textColorId)
        binding.tvTitle.setTextColor(textColor)

        if (todoTask.second.done == false) {
            binding.btnDone.setImageResource(R.drawable.icon_uncheck)
        } else {
            binding.btnDone.setImageResource(R.drawable.icon_check)
        }

        binding.cvItem.setOnClickListener {
            onItemSelected(Actions.DONE, todoTask.first)
        }
    }
}