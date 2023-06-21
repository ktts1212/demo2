package com.example.androiddemo.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.databinding.FragmentPubBinding
import com.example.androiddemo.entities.Note
import com.example.androiddemo.utils.bitmaptoblob
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread

class PubFragment : Fragment() {

    val noteDao=AppDatabase.getDatabase(MainApplicaiton.context).noteDao()
    companion object{
        //0代表有权限没有通过，1代表全部通过
        var isGranted=0
        val TAKE_PHOTO=1
        val CHOOSE_PHOTO=2
    }

    var imageUri: Uri?=null
    private var permissionsList=ArrayList<String>()
    private lateinit var binding: FragmentPubBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPubBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageInput.setOnClickListener {
            if (isGranted==0){
                checkPermissions()
            }
            //Log.d("AAAAAAA","you clicked me")
            else {
                AlertDialog.Builder(this.context).apply {
                    setTitle("请选择图片")
                    setMessage("请选择插入图片方式")
                    setCancelable(false)
                    setPositiveButton("拍照"){
                            dialog,which->
                        call()
                    }
                    setNegativeButton("相册"){
                            dialog,which->
                        openAlbum()
                    }
                    show()
                }
            }
        }

        binding.pubBtn.setOnClickListener {
            thread {

                val note= Note(binding.titleInput.text.toString(), binding.contentInput.text.toString(),
                    bitmaptoblob(binding.imageInput.drawable.toBitmap(100,100,null))
                    ,"张三", tag = "推荐")
                note.id=noteDao.insertNote(note)
            }
            Toast.makeText(MainApplicaiton.context,"发布成功",Toast.LENGTH_SHORT).show()

            thread {
                for (note in noteDao.loadAllNote()){
                    Log.d("AAAAAA",note.toString())
                    Log.d("AAAAAA",note.tag)
                    Log.d("AAAAAAA",note.author)
                }
            }
        }
    }
    fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(MainApplicaiton.context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            permissionsList.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(
                MainApplicaiton.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                MainApplicaiton.context,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!permissionsList.isEmpty()){
            requestPermissionLauncher.launch(
                arrayOf(
                    permissionsList[0],
                    permissionsList[1],
                    permissionsList[2]
                )
            )
        }else{
            isGranted=1
            permissionsList.clear()
        }
    }

    val requestPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permissions:Map<String,Boolean>->
        permissions.entries.forEach{
            if (it.value==false){
                Toast.makeText(MainApplicaiton.context,"您拒绝了该权限${it.key}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openAlbum(){
        val intent=Intent("android.intent.action.GET_CONTENT")
        intent.type="image/*"
        startActivityForResult(intent, CHOOSE_PHOTO)
    }

    fun call(){
        //创建Flie对象，存储拍照后的图片
        val outputImage= File(this.context?.externalCacheDir,"output_image.jpg")
        try {
            if (outputImage.exists()){
                outputImage.delete()
            }
            outputImage.createNewFile()
        }catch (e: Exception){
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT<24){
            imageUri=Uri.fromFile(outputImage)
        }else{
            imageUri=
                FileProvider.getUriForFile(MainApplicaiton.context,"com.example.androiddemo.fileProvider",outputImage)
        }

        //启动相机功能
        val intent= Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(intent, TAKE_PHOTO)
        //launcher.launch(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            TAKE_PHOTO->if (resultCode== ComponentActivity.RESULT_OK){
                try {
                    val bitmap= BitmapFactory.decodeStream(this.requireContext().contentResolver.openInputStream(imageUri!!))
                    binding.imageInput.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            CHOOSE_PHOTO->if (resultCode== ComponentActivity.RESULT_OK){
                if (Build.VERSION.SDK_INT>=19){
                    if (data!=null){
                        handleImageOnKitKat(data)
                    }
                }
            }
        }

    }

    @SuppressLint("Range")
    private fun getImagePath(uri: Uri?, selection: String?): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor = this.requireContext().contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun displayImage(imagePath: String?) {
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.imageInput.setImageBitmap(bitmap)
        } else {
            Toast.makeText(MainApplicaiton.context, "failed to get image", Toast.LENGTH_SHORT).show()
        }
    }



    //4.4后 判断封装情况
    @TargetApi(19)
    private fun handleImageOnKitKat(data: Intent) {
        var imagePath: String? = null
        val uri = data.data
        Log.d("TAG", "handleImageOnKitKat: uri is $uri")
        if (DocumentsContract.isDocumentUri(this.context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                val id = docId.split(":".toRegex()).toTypedArray()[1] // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.path
        }
        displayImage(imagePath) // 根据图片路径显示图片
    }


    private fun handleImageBeforeKitKat(data: Intent) {
        val uri = data.data
        val imagePath = getImagePath(uri, null)
        displayImage(imagePath)
    }
}