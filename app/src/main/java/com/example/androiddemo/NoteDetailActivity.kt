package com.example.androiddemo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androiddemo.databinding.ActivityNoteDetailBinding

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailImageview.setImageBitmap(BitmapFactory.decodeByteArray(intent.getByteArrayExtra("avatar")
        ,0, intent.getByteArrayExtra("avatar")!!.size))
        binding.detailTitle.text=intent.getStringExtra("title")
        binding.detailContent.text=intent.getStringExtra("content")
    }
}