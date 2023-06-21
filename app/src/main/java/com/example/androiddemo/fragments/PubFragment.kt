package com.example.androiddemo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.androiddemo.databinding.FragmentPubBinding
import com.example.androiddemo.utils.MyApplicaiton

class PubFragment : Fragment() {
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
            Log.d("AAAAAAA","you clicked me")
            AlertDialog.Builder(MyApplicaiton.context).apply {
                setTitle("请选择图片")
                setMessage("请选择插入图片方式")
                setCancelable(false)
                setPositiveButton("拍照"){
                    dialog,which->

                }
                setNegativeButton("相册"){
                    dialog,which->
                }
                show()
            }
        }
    }

}