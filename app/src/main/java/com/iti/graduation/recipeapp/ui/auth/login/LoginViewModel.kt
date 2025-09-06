package com.iti.graduation.recipeapp.ui.auth.login

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

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<User?>()
    val loggedInUser: LiveData<User?> get() = _loggedInUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun login(email:String, password:String){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val user = authRepository.loginUser(email,password)
                if(user != null){
                    _loggedInUser.value = user
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