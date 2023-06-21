package com.example.androiddemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androiddemo.R
import com.example.androiddemo.childFragments.MyChildFragment
import com.example.androiddemo.databinding.FragmentMyBinding
import com.google.android.material.tabs.TabLayoutMediator


class MyFragment : Fragment() {
    private val tabTitles= mutableListOf<String>("笔记","收藏","赞过")
    private val fragments= listOf(lazy { MyChildFragment() },lazy { MyChildFragment() },lazy { MyChildFragment() })
    private lateinit var binding: FragmentMyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMyBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter=myviewPaperAdapter()
        binding.myViewPaper.adapter=adapter
        TabLayoutMediator(binding.myTabHeader,binding.myViewPaper){
            tab,position->
            tab.text=tabTitles[position]
        }.attach()
    }

    inner class myviewPaperAdapter:FragmentStateAdapter(this){
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position].value as Fragment
        }
    }
}