package com.cs.noteappcheesycode.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs.noteappcheesycode.models.UserRequest
import com.cs.noteappcheesycode.models.UserResponse
import com.cs.noteappcheesycode.repository.UserRepository
import com.cs.noteappcheesycode.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(
        userName: String,
        emailAddress: String,
        password: String,
        isLogin :Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (!isLogin && TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please Provide Credential")
            return result;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please Provide Valid Email")
            return result
        }
        if (password.length < 5) {
            result = Pair(false, "Password length should be greater than 5")
            return result
        }
        return result
    }

}