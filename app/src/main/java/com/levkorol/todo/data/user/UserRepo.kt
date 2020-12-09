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
        get() = sp.getString(PIN_KEY, "").orEmpty().isNotEmpty()

    override fun setPinCode(pinCode: String) {
        sp.edit().putString(PIN_KEY, pinCode).apply()
    }

    override fun getPinCode(): String {
        return sp.getString(PIN_KEY, "").orEmpty()
    }

    private companion object {
        const val PIN_KEY = "pin_key"
    }
}