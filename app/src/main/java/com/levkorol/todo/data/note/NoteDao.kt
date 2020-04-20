package com.levkorol.todo.data.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levkorol.todo.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<Note>>

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("delete from note where noteId = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * from note WHERE noteId = :key")
    fun get(key: Long): Note?

    @Query("SELECT * FROM note ORDER BY noteId DESC LIMIT 1")
    fun getToNote(): Note?

    @Query("SELECT * from note WHERE noteId = :key")
    fun getNoteId(key: Long): LiveData<Note>
}