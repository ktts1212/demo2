package com.example.androiddemo.configdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androiddemo.dao.NoteDao
import com.example.androiddemo.entities.Note

@Database(version = 1, entities = [Note::class])
abstract class AppDatabase :RoomDatabase(){
    abstract fun noteDao(): NoteDao
    companion object{
        private var instance:AppDatabase?=null

        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java,"app_database")
                .build().apply {
                    instance=this
                }
        }
    }
}