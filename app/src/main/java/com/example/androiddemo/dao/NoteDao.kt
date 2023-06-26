package com.example.androiddemo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.androiddemo.entities.Note

@Dao
interface NoteDao {
   @Insert
   fun insertNote(note: Note):Long

   @Query("select * from Note")
   fun loadAllNote():List<Note>

   @Query("select * from Note where tag=:tag")
   fun queryNotes(tag:String):List<Note>

   @Query("delete from Note where author=:nickname")
   fun deleteByNickname(nickname:String):Int

   @Query("select * from Note where author=:nickname")
   fun queryByNickname(nickname: String):List<Note>
}