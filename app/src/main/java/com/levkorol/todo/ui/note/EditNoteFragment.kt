package com.levkorol.todo.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.levkorol.todo.R

// TODO комментарии к #1:
// TODO 1. newInstance(noteId: Long)
// TODO 2. вписать в поле noteId
// TODO 3. observeNotes, подписываемся в onStart
// TODO 4. notes.firstOrNull { note -> note.description.startsWith("asdsada") }!!, поменять условие на id
// TODO 5. отображаешь в полях
// TODO 6. NoteRepository.update
class EditNoteFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.edit_note_fragment, container, false)
    }
}