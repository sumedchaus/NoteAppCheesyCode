package com.cs.noteappcheesycode.models

import java.io.Serializable

data class NotesResponse(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userId: String
) :Serializable