package com.example.androiddemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androiddemo.childFragments.LikedFragment
import com.example.androiddemo.childFragments.MyNotesFragment
import com.example.androiddemo.databinding.FragmentMyBinding
import com.google.android.material.tabs.TabLayoutMediator


class MyFragment : Fragment() {

    private val tabTitles = mutableListOf<String>("笔记", "赞过")

    private val fragments = lazy { listOf(MyNotesFragment(), LikedFragment()) }

    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = viewPaperAdapter()
        binding.myViewPaper.adapter = adapter
        TabLayoutMediator(binding.myTabHeader, binding.myViewPaper) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    inner class viewPaperAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return fragments.value.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments.value[position] as Fragment
        }
    }
}
