package com.gyimah.lavori.repositories

import android.app.Application
import androidx.arch.core.executor.TaskExecutor
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.gyimah.lavori.constants.Constants
import com.gyimah.lavori.listeners.AccountListener
import com.gyimah.lavori.listeners.LoginListener
import com.gyimah.lavori.models.User
import com.gyimah.lavori.utils.Session
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    application: Application
) {

    private lateinit var loginListener: LoginListener
    private lateinit var accountListener: AccountListener

    private var session: Session = Session.getInstance(application)

    fun setLoginListener(loginListener: LoginListener) {
        this.loginListener = loginListener
    }

    fun setAccountListener(accountListener: AccountListener) {
        this.accountListener = accountListener;
    }

    fun loginWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(TaskExecutors.MAIN_THREAD) { task ->
                val user = task.user

                if (user != null) {
                    getUser(user)
                }else {
                    loginListener.onLoginFailure("Error logging in, please again")
                }

            }
            .addOnFailureListener {
                var message: String? = it.localizedMessage
                if (message != null) {
                    message = "something unexpected happened, please try again"
                }
                loginListener.onLoginFailure(message!!)
            }
    }

    fun loginWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnSuccessListener(TaskExecutors.MAIN_THREAD) {
                val user = it.user

                if (user != null) {
                    getUser(user)
                }else {
                    loginListener.onLoginFailure("Error logging in, please again")
                }
            }
    }

    private fun getUser(firebaseUser: FirebaseUser) {
        firebaseFirestore.collection(Constants.USERS)
            .document(firebaseUser.uid)
            .get()
            .addOnSuccessListener {

                if (it.exists()) {
                    val user = it.toObject<User>()
                    session.saveUser(user = user!!)

                    loginListener.onLoginSuccess()
                }else {
                    // user details not stored yet
                    accountListener.onAccountNotFound()
                }

            }
            .addOnFailureListener {
                loginListener.onLoginFailure("Error fetching user details, please try again later")
            }
    }
}