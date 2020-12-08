package com.levkorol.todo.data.user

import android.content.SharedPreferences

interface UserRepo {

    val hasPinCode: Boolean

    fun setPinCode(pinCode: String)

    fun getPinCode(): String
}


class UserRepoImpl(
    private val sp: SharedPreferences
) : UserRepo {

    override val hasPinCode: Boolean
        get() = false

    override fun setPinCode(pinCode: String) {

    }

    override fun getPinCode(): String {
        return ""
    }
}