package com.art.realtimeapp

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.art.realtimeapp.Actions.Actions
import com.art.realtimeapp.data.FirebaseInstance
import com.art.realtimeapp.databinding.ActivityMainBinding
import com.art.realtimeapp.databinding.DialogAddTaskBinding
import com.art.realtimeapp.recyclerView.TodoAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseInstance = FirebaseInstance(this)

        initUI()
        initListeners()
    }

    private fun initUI() {
        binding.btnUpdate.setOnClickListener {
            showDialogAdd()
        }
        // delete or update in database
        todoAdapter = TodoAdapter { action, reference ->
            when (action) {
                Actions.DELETE -> firebaseInstance.removeFromDatabase(reference)

                Actions.DONE -> firebaseInstance.updateFromDatabase(reference)
            }
        }
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        binding.textViewDate.text = takeDate()
    }

    private fun takeDate(): String {
        val calendar = Calendar.getInstance()

        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val monthName = monthNames[month]
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return "$monthName $day, $year"
    }

    private fun showDialogAdd() {
        val binding = DialogAddTaskBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        binding.btnAddTask.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(this, "You cannot save an empty task", Toast.LENGTH_SHORT).show()
            } else {
                firebaseInstance.writeOnFirebase(title, description)
                dialog.dismiss()
            }
        }

        binding.btnCancelTask.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun initListeners() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = getCleanSnapshot(snapshot)
                todoAdapter.setNewList(data)

                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", error.details)
            }
        }
        firebaseInstance.setupDataListener(postListener)
    }

    private fun getCleanSnapshot(snapshot: DataSnapshot): List<Pair<String, Todo>> {
        // snapshot es lo que nos devuelve la base de datos
        val list = snapshot.children.map { item ->
            Pair(item.key!!, item.getValue(Todo::class.java)!!)
        }
        // pair -> tipo de objecto con dos valores dentro (clave y valor)
        return list
    }

    private fun updateUI() {

        if (todoAdapter.itemCount == 0) {
            binding.rvTasks.visibility = View.GONE
            binding.emptyListImage.visibility = View.VISIBLE
        } else {
            binding.rvTasks.visibility = View.VISIBLE
            binding.emptyListImage.visibility = View.GONE
        }
    }
}