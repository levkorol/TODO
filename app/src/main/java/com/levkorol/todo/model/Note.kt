package com.levkorol.todo.model

import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// TODO комментарии к #2
// TODO 1. тут вроде ок
// TODO 2. обновить логику в *NoteFragment (звездочка должна меняться и сохранять флаг)
// TODO 3. в NotesFragment visibility в зависимости от star

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) var noteId: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "star") var star: Boolean,
    @ColumnInfo(name = "photo") var photo: String

)




















