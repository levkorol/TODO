package com.levkorol.todo.data.note.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levkorol.todo.model.Targets

@Dao
interface TargetDao {
    @Query("SELECT * FROM targets")
    fun getAll(): LiveData<List<Targets>>

    @Insert
    fun insert(targets: Targets)

    @Query("SELECT * FROM targets")
    fun getAllTargetsNow(): List<Targets>

    @Update
    fun update(targets: Targets)

    @Query("delete from targets where id = :id")
    fun deleteById(id: Long)

    @Query("delete from targets")
    fun deleteAll()

    @Delete
    fun delete(targets: List<Targets> )

    @Query("SELECT * from targets WHERE id = :key")
    fun get(key: Long): Targets?

    @Query("SELECT * FROM targets ORDER BY id DESC LIMIT 1")
    fun getToTargets(): Targets?

    @Query("SELECT * from targets WHERE id = :key")
    fun getTargetsId(key: Long): LiveData<Targets>
}