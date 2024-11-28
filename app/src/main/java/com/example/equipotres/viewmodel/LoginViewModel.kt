package com.example.equipotres.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.equipotres.model.UserRequest
import com.example.equipotres.model.UserResponse
import com.example.equipotres.repository.LoginRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository : LoginRepository
) : ViewModel() {

    private val _isRegister = MutableLiveData<UserResponse>()
    val isRegister: LiveData<UserResponse> = _isRegister
    fun registerUser(userRequest: UserRequest) {
        repository.registerUser(userRequest) { userResponse ->
            _isRegister.value = userResponse
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {
        repository.loginUser(email, pass) { isLoginResponse ->
            isLogin(isLoginResponse)
        }
    }


    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        if (email != null) {
            isEnableView(true)
        } else {
            isEnableView(false)
        }
    }
}