package com.example.androiddemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.androiddemo.config_database.AppDatabase
import com.example.androiddemo.databinding.ActivityMainBinding
import com.example.androiddemo.entities.Note
import com.example.androiddemo.fragments.HomeFragment
import com.example.androiddemo.fragments.MyFragment
import com.example.androiddemo.fragments.PubFragment
import com.example.androiddemo.utils.bitmaptoblob


class MainActivity : AppCompatActivity() {

//    companion object{
//        val noteDao=AppDatabase.getDatabase(this).noteDao()
//    }

    private val homeFragment by lazy {
        HomeFragment()
    }

    private val pubFragment by lazy {
        PubFragment()
    }

    private val myFragment by lazy {
        MyFragment()
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager=supportFragmentManager
        val transcation=fragmentManager.beginTransaction()
        transcation.replace(R.id.fragment_container,homeFragment)
        transcation.commit()
        binding.bottmoNavigationView.setOnItemSelectedListener {
                val transcation=fragmentManager.beginTransaction()
                when(it.itemId){
                    R.id.home->{
                        transcation.replace(R.id.fragment_container,homeFragment)
                        transcation.commit()
                        return@setOnItemSelectedListener true
                    }
                    R.id.publish->{
                        transcation.replace(R.id.fragment_container,pubFragment)
                        transcation.commit()
                        return@setOnItemSelectedListener true
                    }
                    R.id.my->{
                        transcation.replace(R.id.fragment_container,myFragment)
                        transcation.commit()
                        return@setOnItemSelectedListener true
                    }
                    else->false
                }
        }
    }

//    fun addData(){
//        val bitmap=(resources.getDrawable(R.drawable.img_1,null)).toBitmap()
//        val note1= Note("家人们谁懂啊", "今天要去吃好吃的", bitmaptoblob()
//    }
}