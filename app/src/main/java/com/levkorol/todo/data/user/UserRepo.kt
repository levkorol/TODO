package com.levkorol.todo.data.user

import android.content.SharedPreferences

interface UserRepo {

    var needToRequestPinCode: Boolean
    val hasPinCode: Boolean
    fun setPinCode(pinCode: String)
    fun getPinCode(): String

    var needToRequestDarkTheme: Boolean
    fun setDarkTheme(theme: Boolean)
    fun getDarkTheme(): Boolean

    var hasVisited: Boolean
    fun setVisited(visit: Boolean)

}


class UserRepoImpl(
    private val sp: SharedPreferences
) : UserRepo {

    override val hasPinCode: Boolean
        get() = sp.getString(PIN_KEY, "").orEmpty().isNotEmpty()

    override var needToRequestPinCode: Boolean
        get() = sp.getBoolean(REQUEST_PIN_KEY, false)
        set(value) {
            sp.edit().putBoolean(REQUEST_PIN_KEY, value).apply()
        }

    override fun setPinCode(pinCode: String) {
        needToRequestPinCode = true
        sp.edit().putString(PIN_KEY, pinCode).apply()
    }

    override fun getPinCode(): String {
        return sp.getString(PIN_KEY, "").orEmpty()
    }


    override var needToRequestDarkTheme: Boolean
        get() = sp.getBoolean(DARK_THEME_KEY, false)
        set(value) {
            sp.edit().putBoolean(DARK_THEME_KEY, value).apply()
        }

    override fun setDarkTheme(theme: Boolean) {
        needToRequestDarkTheme = true
        sp.edit().putBoolean(DARK_THEME_KEY, theme).apply()
    }

    override fun getDarkTheme(): Boolean {
        return sp.getBoolean(DARK_THEME_KEY, false)
    }


    override var hasVisited: Boolean
        get() = sp.getBoolean(HAS_VISITED, false)
        set(value) {
            sp.edit().putBoolean(HAS_VISITED, value).apply()
        }

    override fun setVisited(visit: Boolean) {
        hasVisited = true
        sp.edit().putBoolean(HAS_VISITED, visit).apply()
    }

    enum class AppThemes {
        DARK, REGULAR
    }

    private companion object {
        const val HAS_VISITED = "has_visited"
        const val PIN_KEY = "pin_key"
        const val REQUEST_PIN_KEY = "request_pin_key"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}