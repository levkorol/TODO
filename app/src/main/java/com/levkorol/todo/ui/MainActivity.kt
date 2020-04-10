package com.levkorol.todo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.levkorol.todo.R
import com.levkorol.todo.ui.notes.NotesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNotes()
    }

    private fun showNotes() {
        val fragment = NotesFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

}
