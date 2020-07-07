package com.levkorol.todo.data.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levkorol.todo.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<Note>>

    @Insert
    fun insert(note: Note): Long

    @Update
    fun update(note: Note)

    @Query("delete from note where id = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(note:List <Note> )

    @Query("SELECT * from note WHERE id = :key")
    fun get(key: Long): Note?

    @Query("SELECT * FROM note ORDER BY id DESC LIMIT 1")
    fun getToNote(): Note?

    @Query("SELECT * from note WHERE id = :key")
    fun getNoteId(key: Long): LiveData<Note>
}