package com.example.androiddemo.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.R
import com.example.androiddemo.entities.Follow

class FollowAdapter(private val followList:List<Follow>):RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val followImage:ImageView=view.findViewById(R.id.follow_imageview)
        val nickname:TextView=view.findViewById(R.id.follow_nickname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.follow_item,parent,false)
        val viewHolder=ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follow=followList[position]
        holder.followImage.setImageBitmap(
            BitmapFactory.decodeByteArray(
                follow.avatar,0,follow.avatar.size
            )
        )
        holder.nickname.text=follow.nickname
    }
}