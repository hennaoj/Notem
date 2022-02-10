package com.example.notem.data.user

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {

    @Query("SELECT * FROM users")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * from users where userId = :id")
    fun getById(id: Int): User?

    @Query("UPDATE users SET loggedin=:loggedIn WHERE userId = :id")
    fun logUser(loggedIn: Boolean?, id: Long)

    @Insert
    suspend fun insert(item: User)

    @Update
    suspend fun update(item: User)

    @Delete
    suspend fun delete(item: User)

}