package com.example.notem.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "username")
    var userName: String,

    @ColumnInfo(name = "password")
    var passWord: String,

    @ColumnInfo(name = "firstname")
    var first: String,

    @ColumnInfo(name = "lastname")
    var last: String,

    @ColumnInfo(name = "loggedin")
    var loggedIn: Boolean = false

)
