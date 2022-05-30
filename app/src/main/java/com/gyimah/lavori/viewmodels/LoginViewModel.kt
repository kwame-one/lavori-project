package com.gyimah.lavori.viewmodels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.gyimah.lavori.listeners.AccountListener
import com.gyimah.lavori.listeners.LoginListener
import com.gyimah.lavori.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), LoginListener, AccountListener {


    val successState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<String>()
    val accountState = MutableLiveData<Int>()

    init {
        authRepository.setLoginListener(this)
        authRepository.setAccountListener(this)
    }

    fun loginWithEmail(email: String, password: String) {

        when {
            email.trim().isEmpty() -> {
                errorState.postValue("Email cannot be empty")
            }
            password.trim().isEmpty() -> {
                errorState.postValue("Password cannot be empty")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                errorState.postValue("Email is invalid")
            }
            else -> {
                authRepository.loginWithEmail(email, password)
            }
        }

    }

    fun loginWithGoogle(idToken: String) {
        authRepository.loginWithGoogle(idToken)
    }

    override fun onLoginSuccess() {
        successState.postValue(true)
    }

    override fun onLoginFailure(message: String) {
        successState.postValue(false)
        errorState.postValue(message)
    }

    override fun onAccountNotFound() {
        accountState.postValue(1)
    }
}