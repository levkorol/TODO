package com.levkorol.todo.data

import android.content.Context
import com.levkorol.todo.NotesApplication
import com.levkorol.todo.data.user.UserRepo
import com.levkorol.todo.data.user.UserRepoImpl

object DataProvider {

    private val MY_SETTINGS = "MY_SETTINGS"


    val sharedPrefs by lazy {
        NotesApplication.ctx.getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE)
    }

    val userRepo: UserRepo by lazy {
        UserRepoImpl(sharedPrefs)
    }
}