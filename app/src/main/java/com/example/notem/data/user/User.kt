package com.example.notem.data.user

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
    var loggedIn: Boolean = false,

    @ColumnInfo(name = "location_x")
var userX: Double,

@ColumnInfo(name = "location_y")
var userY: Double,

)
