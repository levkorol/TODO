package com.levkorol.todo.data.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levkorol.todo.model.Folder

@Dao
interface FolderDao {
    @Query("SELECT * FROM folder")
    fun getAll(): LiveData<List<Folder>>

    @Insert
    fun insert(folder: Folder)

    @Update
    fun update(folder: Folder)

    @Query("delete from folder where id = :id")
    fun deleteById(id: Long)

    @Delete
    fun delete(folder: Folder)

    @Query("SELECT * from folder WHERE id = :key")
    fun get(key: Long): Folder?

    @Query("SELECT * FROM folder ORDER BY id DESC LIMIT 1")
    fun getToFolder(): Folder?

    @Query("SELECT * from folder WHERE id = :key")
    fun getFolderId(key: Long): LiveData<Folder>
}