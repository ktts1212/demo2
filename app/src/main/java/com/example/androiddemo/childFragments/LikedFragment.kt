package com.example.androiddemo.childFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.adapters.NoteAdapter
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.dao.NoteDao
import com.example.androiddemo.databinding.FragmentLikedBinding
import com.example.androiddemo.entities.Note
import kotlin.concurrent.thread


class LikedFragment : Fragment() {

    companion object{
        private var noteListInitialize=0
    }

    private lateinit var binding:FragmentLikedBinding

    private lateinit var noteDao: NoteDao

    private var noteList=ArrayList<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteDao=AppDatabase.getDatabase(MainApplicaiton.context).noteDao()
        initNoteList()
        binding= FragmentLikedBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager=LinearLayoutManager(this.requireContext())
        layoutManager.orientation=LinearLayoutManager.VERTICAL
        binding.likedRecyclerview.layoutManager=layoutManager
        val adapter=NoteAdapter(noteList)
        binding.likedRecyclerview.adapter=adapter
        binding.likedSwiperefresh.setOnRefreshListener {
            refreshNoteList(adapter)
        }
    }

    fun initNoteList(){
        //noteList.clear()
        if (noteListInitialize==0||noteList.isEmpty()){
            noteListInitialize=1
            thread {
                for (note in noteDao.queryByNickname("小星星")) {
                    Log.d("LikedFragment", note.author)
                    noteList.add(note)
                }
                for (note in noteDao.queryByNickname("燥3岁")) {
                    noteList.add(note)
                    Log.d("Liked",note.author)
                }
            }
        }
    }

    fun refreshNoteList(adapter: NoteAdapter){
        initNoteList()
        adapter.notifyDataSetChanged()
        binding.likedSwiperefresh.isRefreshing=false
    }

//    override fun onStart() {
//        super.onStart()
//        if (!noteList.isEmpty()){
//            noteList.clear()
//        }
//    }

}