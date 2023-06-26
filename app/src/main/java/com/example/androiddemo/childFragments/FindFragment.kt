package com.example.androiddemo.childFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androiddemo.MainApplicaiton
import com.example.androiddemo.adapters.NoteAdapter
import com.example.androiddemo.configdatabase.AppDatabase
import com.example.androiddemo.dao.NoteDao
import com.example.androiddemo.databinding.FragmentFindBinding
import com.example.androiddemo.entities.Note
import kotlin.concurrent.thread

class FindFragment : Fragment() {

    companion object{
        private var noteListInitialize=0
    }

    private final val TAG="FindFragment"

    private var noteList = ArrayList<Note>()

    private  val noteDao: NoteDao=AppDatabase.getDatabase(MainApplicaiton.context).noteDao()

    private lateinit var binding: FragmentFindBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView")
        binding = FragmentFindBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initList()
        super.onViewCreated(view, savedInstanceState)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.findRecyclerView.layoutManager = staggeredGridLayoutManager
        val adapter = NoteAdapter(noteList)
        binding.findRecyclerView.adapter = adapter
        binding.findSwipeRefresh.setOnRefreshListener {
            refreshNoteList(adapter)
        }
    }

    fun initList() {
        if (noteListInitialize==0||noteList.isEmpty()){
            noteListInitialize=1
            thread {
                for (note in noteDao.queryNotes("推荐")) {
                    noteList.add(note)
                }
            }
        }
    }

    private fun refreshNoteList(adapter: NoteAdapter){
        initList()
        adapter.notifyDataSetChanged()
        binding.findSwipeRefresh.isRefreshing=false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG,"onDEstoryview....")
        //noteList.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"OnDestory.....")
        //noteList.clear()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"OnStart.....")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"OnResume.....")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"OnPause.....")
        noteListInitialize=0
        noteList.clear()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        Log.d(TAG,"OnAttach.....")
    }

    override fun onStop() {
        super.onStop()
        //noteListInitialize=0
        Log.d(TAG,"OnStop.....")
    }

}