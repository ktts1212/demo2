package com.example.androiddemo.childFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.adapters.FollowAdapter
import com.example.androiddemo.adapters.NoteAdapter
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.databinding.FragmentFollowBinding
import com.example.androiddemo.entities.Follow
import com.example.androiddemo.entities.Note
import kotlin.concurrent.thread

class FollowFragment : Fragment() {

    private val followDao = AppDatabase.getDatabase(MainApplicaiton.context).followDao()

    private val noteDao = AppDatabase.getDatabase(MainApplicaiton.context).noteDao()

    companion object{
        var followListInitialize=0

        var noteListInitialize=0

        var followList = ArrayList<Follow>()

        var noteList=ArrayList<Note>()
    }

    private lateinit var binding: FragmentFollowBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initNoteList()
        initFollowList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(MainApplicaiton.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.followRecyclerView.layoutManager = layoutManager
        val adapter = FollowAdapter(followList)
        binding.followRecyclerView.adapter = adapter
        val layoutManager1=LinearLayoutManager(MainApplicaiton.context)
        layoutManager1.orientation=LinearLayoutManager.VERTICAL
        binding.followNoteRecyclerView.layoutManager=layoutManager1
        val adapter1=NoteAdapter(noteList)
        binding.followNoteRecyclerView.adapter=adapter1
        binding.followSwipeRefresh.setOnRefreshListener {
            refreshLayout(adapter)
        }
        binding.followNoteSwipeRefrech.setOnRefreshListener {
            refreshNote(adapter1)
        }
    }

    fun initNoteList(){
        if (noteListInitialize==0|| noteList.size!=3){
            noteListInitialize=1
            thread {
                for (note in noteDao.queryByNickname("稳妥先生"))
                    noteList.add(note)
            }
            thread {
                for (note in noteDao.queryByNickname("青青子衿"))
                    noteList.add(note)
            }
            thread {
                for (note in noteDao.queryByNickname("吐司男"))
                    noteList.add(note)
            }
        }
    }

    fun initFollowList() {
        if (followListInitialize==0|| followList.isEmpty()){
            followListInitialize=1
            thread {
                for (follow in followDao.loadAllFollow()) {
                    followList.add(follow)
                }
            }
        }
    }

    private fun refreshLayout(adapter: FollowAdapter){
        initFollowList()
        adapter.notifyDataSetChanged()
        binding.followSwipeRefresh.isRefreshing=false
    }

    private fun refreshNote(adapter: NoteAdapter){
        initNoteList()
        adapter.notifyDataSetChanged()
        binding.followNoteSwipeRefrech.isRefreshing=false
    }
}