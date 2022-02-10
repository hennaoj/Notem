package com.example.notem.data.user

import androidx.lifecycle.LiveData

class UserRepository(private val usersDao: UsersDao) {

    val readAllData: LiveData<List<User>> = usersDao.getAll()

    suspend fun addUser(user: User) {
        usersDao.insert(user)
    }

    suspend fun updateUser(user: User) {
        usersDao.update(user)
    }

    fun logUser(loggedIn: Boolean, id: Long) {
        usersDao.logUser(loggedIn, id)
    }

    suspend fun deleteUser(user: User) {
        usersDao.delete(user)
    }

}
