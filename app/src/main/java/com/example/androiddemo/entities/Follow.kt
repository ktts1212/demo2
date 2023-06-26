package com.example.androiddemo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Follow(val avatar:ByteArray, val nickname:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Long=0
}