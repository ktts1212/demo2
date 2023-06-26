package com.example.androiddemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.databinding.ActivityMainBinding
import com.example.androiddemo.entities.Follow
import com.example.androiddemo.entities.Note
import com.example.androiddemo.fragments.HomeFragment
import com.example.androiddemo.fragments.MyFragment
import com.example.androiddemo.fragments.PubFragment
import com.example.androiddemo.service.NetworkStateService
import com.example.androiddemo.utils.bitmaptoblob
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    companion object{
        //0 未初始化，1 已初始化
        var tableFollowInitialize=0
        var tableNoteInitialize=0
    }

    private val followDao = AppDatabase.getDatabase(MainApplicaiton.context).followDao()

    private val noteDao = AppDatabase.getDatabase(this).noteDao()

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainAc", tableFollowInitialize.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkPermission()
        } else {
            //开启网路检测服务
            intent = Intent(this, NetworkStateService::class.java)
            startService(intent)
        }
        val fragmentManager = supportFragmentManager
        val transcation = fragmentManager.beginTransaction()
        transcation.replace(R.id.fragment_container, homeFragment)
        transcation.commit()
        binding.bottmoNavigationView.setOnItemSelectedListener {
            val transcation = fragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.home -> {
                    transcation.replace(R.id.fragment_container, homeFragment)
                    transcation.commit()
                    return@setOnItemSelectedListener true
                }

                R.id.publish -> {
                    transcation.replace(R.id.fragment_container, pubFragment)
                    transcation.commit()
                    return@setOnItemSelectedListener true
                }

                R.id.my -> {
                    transcation.replace(R.id.fragment_container, myFragment)
                    transcation.commit()
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            //开启网路检测服务
            intent = Intent(this, NetworkStateService::class.java)
            startService(intent)
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //开启网路检测服务
                intent = Intent(this, NetworkStateService::class.java)
                startService(intent)
            } else {
                Toast.makeText(
                    MainApplicaiton.context,
                    "您需要通知权限用于汇报网络监听",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun initDatabase() {

        if (tableFollowInitialize==0&& tableNoteInitialize==0){

            tableNoteInitialize=1

            tableFollowInitialize=1

            val note1=Note("GitHub上“千金难求”，SpringBoot全彩手册","内容涵盖SpringMVC，Mybatis plus，" +
                    "SpringData JPA等主流框架，还涉及单元测试，异常处理等。", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note1)),"稳妥先生",islike = false
            )

            val note2=Note("中国的IT人才缺口更大？缺少3000万？","都说日本缺IT人才大概80万，而据人瑞" +
                    "报告中国的缺口为3000万人，增长最快的用人需求是人工智能基盘和智能制造", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note2)),"青青子衿",islike = false
            )

            val note3=Note("郑州人才公寓:紫辰美寓即将开抢！！！","啊吐去管城区紫辰美寓了，替大家考虑一下！！" +
                    "啊吐个人觉得，能抢人才公寓尽量要抢，毕竟环境价格方面都非常划算！！", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note3)),"吐司男",islike = false
            )

            val note4=Note("夏日养生小常识","冰镇冷饮不仅仅会降低胃动力，往上寒气入心，入肺，往下可以" +
                    "影响到大肠，小肠", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note4)),"张三李四王五i", islike = false
            )

            val note5=Note("全球十大军用运输机","图片仅用于个人学习与交流或个人欣赏用途(禁转发搬运/禁商用)", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note5)),"小星星", islike = true
            )

            val note6=Note("笑笑我呀，今天要开始上全班了哦","懂事的笑猪猪一人能扛起宝家半边天了，" +
                    "替母上班，像你爸爸学习，加油呀笑宝", bitmaptoblob(BitmapFactory.
            decodeResource(resources,R.drawable.note6)),"燥3岁", islike = true
            )

            val follow1 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user1)),
                "稳妥先生"
            )
            val follow2 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user2)),
                "南山抠脚汉"
            )
            val follow3 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user3)),
                "青青子衿"
            )
            val follow4 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user4)),
                "吐司男"
            )
            val follow5 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user5)),
                "我酷毙了吗"
            )
            val follow6 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user6)),
                "街道男孩"
            )
            val follow7 = Follow(
                bitmaptoblob(BitmapFactory.decodeResource(resources, R.drawable.user7)),
                "她是纯氧."
            )

            thread {
                follow1.id = followDao.insertFollow(follow1)
                follow2.id = followDao.insertFollow(follow2)
                follow3.id = followDao.insertFollow(follow3)
                follow4.id = followDao.insertFollow(follow4)
                follow5.id = followDao.insertFollow(follow5)
                follow6.id = followDao.insertFollow(follow6)
                follow7.id = followDao.insertFollow(follow7)
                note1.id=noteDao.insertNote(note1)
                note2.id=noteDao.insertNote(note2)
                note3.id=noteDao.insertNote(note3)
                note4.id=noteDao.insertNote(note4)
                note5.id=noteDao.insertNote(note5)
                note6.id=noteDao.insertNote(note6)
            }
        }else{
         Log.d("MainActivity","数据库已完成初始化")
        }
    }


    override fun onStop() {
        super.onStop()
        thread {
            followDao.deleteAllData()
            noteDao.deleteByNickname("稳妥先生")
            noteDao.deleteByNickname("青青子衿")
            noteDao.deleteByNickname("吐司男")
            noteDao.deleteByNickname("张三李四王五i")
            noteDao.deleteByNickname("燥3岁")
            noteDao.deleteByNickname("小星星")
        }
        Log.d("MainAc","已被销毁")

    }

    override fun onStart() {
        super.onStart()
        Log.d("MainAc","初始化了数据库")
        initDatabase()
    }
}