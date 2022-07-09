package com.cs.noteappcheesycode.api

import com.cs.noteappcheesycode.models.NoteRequest
import com.cs.noteappcheesycode.models.NotesResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteApi {

    @GET("/note")
    suspend fun getNotes(): Response<List<NotesResponse>>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NotesResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String,
        @Body noteRequest: NoteRequest
    ): Response<NotesResponse>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(
        @Path("noteId") noteId: String
    ): Response<NotesResponse>

}