package com.example.androiddemo.childFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.adapters.NoteAdapter
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.dao.NoteDao
import com.example.androiddemo.databinding.FragmentLocalBinding
import com.example.androiddemo.entities.Note
import kotlin.concurrent.thread

class LocalFragment : Fragment() {

    companion object{
        var noteListInitialize=0
    }

    private lateinit var noteDao: NoteDao

    private var noteList = ArrayList<Note>()

    private lateinit var binding: FragmentLocalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        noteDao = AppDatabase.getDatabase(MainApplicaiton.context).noteDao()
        binding = FragmentLocalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.localRecyclerView.layoutManager = staggeredGridLayoutManager
        val adapter = NoteAdapter(noteList)
        binding.localRecyclerView.adapter = adapter
        binding.localSwipeRefresh.setOnRefreshListener {
            refreshNoteList(adapter)
        }
    }

    fun initList() {
        if (noteListInitialize==0){
            noteListInitialize=1
            thread {
                for (note in noteDao.queryNotes("郑州")) {
                    noteList.add(note)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        noteListInitialize=0
        noteList.clear()
    }

    private fun refreshNoteList(adapter: NoteAdapter){
        initList()
        adapter.notifyDataSetChanged()
        binding.localSwipeRefresh.isRefreshing=false
    }


}