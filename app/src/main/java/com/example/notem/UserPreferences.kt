package com.example.notem

import android.content.Context

class UserPreferences(context: Context) {

    private val setUsername = "Username"
    private val setPassword = "Password"
    val preference = context.getSharedPreferences("prefs",Context.MODE_PRIVATE)

    fun getUsername() : String? {
        return preference.getString(setUsername, "test_user")
    }

    fun getPassword() : String? {
        return preference.getString(setPassword, "password123")
    }

}