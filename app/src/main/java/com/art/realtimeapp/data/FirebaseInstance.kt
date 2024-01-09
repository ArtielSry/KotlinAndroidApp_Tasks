package com.art.realtimeapp.data

import android.content.Context
import android.util.Log
import com.art.realtimeapp.Todo
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.random.Random

class FirebaseInstance(context: Context) {
    private val database =
        Firebase.database("https://realtimebasic-18b7a-default-rtdb.europe-west1.firebasedatabase.app")

    // myRef = id of my db
    private val myRef = database.reference

    // init firebase
    init {
        FirebaseApp.initializeApp(context)
    }

    fun writeOnFirebase(title: String, description: String) {
        val randomValue = Random.nextInt(1, 200).toString()
        // save multiple object
        val newItem = myRef.push()
        // save value on my db
        newItem.setValue(Todo(title, description))

        // Log.i("Numero random", randomValue)
    }

    fun setupDataListener(postListener: ValueEventListener) {
        database.reference.addValueEventListener(postListener)
    }

    fun removeFromDatabase(reference: String) {
        myRef.child(reference).removeValue()
    }

    fun updateFromDatabase(reference: String) {
        val referenceNode = myRef.child(reference)

        referenceNode.child("done").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentValue = dataSnapshot.getValue(Boolean::class.java) ?: false
                referenceNode.child("done").setValue(!currentValue)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}