package com.gyimah.lavori.utils

import android.content.Context
import android.content.SharedPreferences
import com.gyimah.lavori.models.User
import javax.inject.Singleton

@Singleton
class Session {

    companion object Singleton {

        private val instance: Session? = null
        private lateinit var sharedPreferences: SharedPreferences

        fun getInstance(context: Context): Session {
            sharedPreferences = context.applicationContext.getSharedPreferences("lavoir_pref", Context.MODE_PRIVATE)

            return instance ?: synchronized(this) {
                instance ?: Session()
            }
        }

    }

    fun saveUser(user: User) {
        with(sharedPreferences.edit()) {

            apply()
        }
    }
}
