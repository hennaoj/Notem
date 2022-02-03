package com.example.notem.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notem.data.User
import com.example.notem.data.UserDatabase
import com.example.notem.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData

        loadUsersFromDb()
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user = user)
        }
    }

    private fun loadUsersFromDb() {
        val list = mutableListOf(
            User(userName = "tester", passWord = "password123", first = "Joe", last = "Average")
        )
        viewModelScope.launch {
            list.forEach { user -> repository.addUser(user) }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user = user)
        }
    }

    fun logUser(loggedIn: Boolean, id: Long) {
        viewModelScope.launch (Dispatchers.IO) {
            repository.logUser(loggedIn = loggedIn, id = id)
        }
    }
}

class UserViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}