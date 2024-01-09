package com.art.realtimeapp.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.art.realtimeapp.Actions.Actions
import com.art.realtimeapp.R
import com.art.realtimeapp.Todo

class TodoAdapter(
    private var todoList: List<Pair<String, Todo>> = emptyList(),
    private val onItemSelected: (Actions, String) -> Unit
) :
    RecyclerView.Adapter<TodoViewHolder>() {

    fun setNewList(newList: List<Pair<String, Todo>>) {
        todoList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        )
    }

    override fun getItemCount(): Int = todoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todoList[position], onItemSelected)
    }
}