package com.levkorol.todo.data.note.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.levkorol.todo.model.Schedule


@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule")
    fun getAllScheduleNow(): List<Schedule>

    @Insert
    fun insert(schedule: Schedule): Long

    @Query("delete from schedule")
    fun deleteAll()

    @Update
    fun update(schedule: Schedule)

    @Query("delete from schedule where id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * from schedule WHERE id = :key")
    fun get(key: Long): Schedule?

    @Query("SELECT * FROM schedule ORDER BY id DESC LIMIT 1")
    fun getToSchedule(): Schedule?

    @Query("SELECT * from schedule WHERE id = :key")
    fun getId(key: Long): LiveData<Schedule>
}