package com.example.androiddemo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(val title:String,val content:String,val image:ByteArray,val author:String="张三",val tag:String="推荐") {
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
}