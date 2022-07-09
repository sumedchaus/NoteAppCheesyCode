package com.cs.noteappcheesycode.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs.noteappcheesycode.api.NoteApi
import com.cs.noteappcheesycode.models.NoteRequest
import com.cs.noteappcheesycode.models.NotesResponse
import com.cs.noteappcheesycode.models.UserResponse
import com.cs.noteappcheesycode.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteApi: NoteApi) {


    private val _notesLiveData = MutableLiveData<NetworkResult<List<NotesResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NotesResponse>>>
        get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something went Wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleResponse(response, "Note Created")

    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleResponse(response, "Note Deleted")

    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId, noteRequest)
        handleResponse(response, "Note Update")

    }

    private fun handleResponse(response: Response<NotesResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(message))
        } else {
            _statusLiveData.postValue(NetworkResult.Error(message))
        }
    }

}