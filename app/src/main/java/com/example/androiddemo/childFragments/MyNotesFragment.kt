package com.example.androiddemo.childFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.R
import com.example.androiddemo.adapters.NoteAdapter
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.dao.NoteDao
import com.example.androiddemo.databinding.FragmentMyNoteBinding
import com.example.androiddemo.entities.Note
import kotlin.concurrent.thread

class MyNotesFragment : Fragment() {

    companion object{
        private var noteListInitialize=0
    }

    private lateinit var binding:FragmentMyNoteBinding

    private var noteList=ArrayList<Note>()

    private lateinit var noteDao:NoteDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteDao=AppDatabase.getDatabase(MainApplicaiton.context).noteDao()
        binding= FragmentMyNoteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        val layoutManager=LinearLayoutManager(this.requireContext())
        layoutManager.orientation=LinearLayoutManager.VERTICAL
        binding.myNotesRecyclerview.layoutManager=layoutManager
        val adapter=NoteAdapter(noteList)
        binding.myNotesRecyclerview.adapter=adapter
        binding.myNotesSwipeRefresh.setOnRefreshListener {
            refreshNoteList(adapter)
        }
    }

    fun initList(){
        if (noteListInitialize==0||noteList.isEmpty()){
             noteListInitialize=1
            thread {
                for (note in noteDao.queryByNickname("张三李四王五i")){
                    noteList.add(note)
                }
            }
        }
    }

    fun refreshNoteList(adapter: NoteAdapter){
        initList()
        adapter.notifyDataSetChanged()
        binding.myNotesSwipeRefresh.isRefreshing=false
    }
}