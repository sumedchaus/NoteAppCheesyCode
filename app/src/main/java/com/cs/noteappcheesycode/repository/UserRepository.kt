package com.cs.noteappcheesycode.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs.noteappcheesycode.api.UserApi
import com.cs.noteappcheesycode.models.UserRequest
import com.cs.noteappcheesycode.models.UserResponse
import com.cs.noteappcheesycode.utils.Constants.TAG
import com.cs.noteappcheesycode.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    // assign with Network result to use Network result class in further process
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signUp(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {

        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signIn(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            // only successful response is directly convert from json to java/ kotlin object
            // to convert the json format in java/ kotlin object
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
    }




}