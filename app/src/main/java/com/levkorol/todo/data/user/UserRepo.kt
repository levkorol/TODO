package com.levkorol.todo.data.user

import android.content.SharedPreferences

interface UserRepo {

    var needToRequestPinCode: Boolean

    val hasPinCode: Boolean

    fun setPinCode(pinCode: String)

    fun getPinCode(): String
}


class UserRepoImpl(
    private val sp: SharedPreferences
) : UserRepo {

    override var needToRequestPinCode: Boolean
        get() = sp.getBoolean(REQUEST_PIN_KEY, true)
        set(value) {
            sp.edit().putBoolean(REQUEST_PIN_KEY, value).apply()
        }

    override val hasPinCode: Boolean
        get() = sp.getString(PIN_KEY, "").orEmpty().isNotEmpty()

    override fun setPinCode(pinCode: String) {
        needToRequestPinCode = true
        sp.edit().putString(PIN_KEY, pinCode).apply()
    }

    override fun getPinCode(): String {
        return sp.getString(PIN_KEY, "").orEmpty()
    }

    private companion object {
        const val PIN_KEY = "pin_key"
        const val REQUEST_PIN_KEY = "request_pin_key"
    }
}