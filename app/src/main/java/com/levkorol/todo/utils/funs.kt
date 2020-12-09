package com.levkorol.todo.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.datepicker.MaterialDatePicker
import com.levkorol.todo.R
import java.text.SimpleDateFormat
import java.util.*


fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

//открыть активити
fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

//из активити в фрагмент
fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    supportFragmentManager.beginTransaction()
        .also { transition ->
            if (addStack) {
                transition.addToBackStack(null)
            }
        }
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .replace(R.id.fragmentContainer, fragment)
        .commit()
}

//во фрагментах
fun Fragment.replaceFragment(fragment: Fragment) {
    activity?.supportFragmentManager?.beginTransaction()
        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ?.addToBackStack(null)
        ?.replace(R.id.fragmentContainer, fragment)
        ?.commit()
}


fun hideKeyboard(activity: AppCompatActivity) {
    val imm: InputMethodManager =
        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
}


fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun openDatePicker(fragment: FragmentManager, data: (dateStr: String) -> Unit) {
    val builder = MaterialDatePicker.Builder.datePicker()
    val picker: MaterialDatePicker<Long> = builder.build()
    var dateString = "1"
    picker.addOnPositiveButtonClickListener { unixDate ->
        dateString = dateToString(unixDate)
        data(dateString)
    }
    picker.show(fragment, picker.toString())
}

fun dateToString(date: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy ", Locale.getDefault())
    return dateFormat.format(date)
}
