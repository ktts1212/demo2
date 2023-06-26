package com.example.androiddemo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.androiddemo.entities.Follow

@Dao
interface FollowDao {
    @Insert
    fun insertFollow(follow: Follow):Long

    @Query("select * from Follow")
    fun loadAllFollow():List<Follow>

    @Query("delete from Follow")
    fun deleteAllData():Int
}