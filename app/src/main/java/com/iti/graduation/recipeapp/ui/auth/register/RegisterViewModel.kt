package com.iti.graduation.recipeapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.graduation.recipeapp.data.model.User
import com.iti.graduation.recipeapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun register(username:String,email:String,password:String){
        val newUser = User(name = username, email = email, password = password)
        viewModelScope.launch {
            try{
                val result = authRepository.registerUser(newUser)
                if(result){
                    _error.value = ""
                    _isSuccess.value = true
                }
                else{
                    _error.value = "Check your Email and Password"
                }
            }
            catch (e: Exception){
                _error.value = "${e.message}"
            }
            finally {
                _isLoading.value = false
            }
        }
    }


}