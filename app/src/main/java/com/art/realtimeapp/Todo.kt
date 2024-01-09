package com.art.realtimeapp

// Model to send to Firebase
data class Todo(
    val title: String? = "",
    val description: String? = "",
    val done: Boolean? = false
)