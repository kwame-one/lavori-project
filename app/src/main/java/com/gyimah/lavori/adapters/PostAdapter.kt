package com.gyimah.lavori.adapters

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.gyimah.lavori.R
import com.gyimah.lavori.listeners.ItemClickListener
import com.gyimah.lavori.models.Post
import javax.inject.Inject

class PostAdapter @Inject constructor(application: Application) : Adapter<PostAdapter.PostViewHolder>() {

    private lateinit var listener: ItemClickListener

    private val posts: MutableList<Post> = mutableListOf();

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
    }

    fun getPosts() = this.posts


    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item_row, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            listener.onItemClicked(it, position)
        }

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(itemView: View) : ViewHolder(itemView) {

        val posterImage = itemView.findViewById<ImageView>(R.id.poster_image)
        val posterName = itemView.findViewById<TextView>(R.id.poster_name)
        val posterHeadline = itemView.findViewById<TextView>(R.id.poster_headline)
        val content = itemView.findViewById<TextView>(R.id.content)
        val image = itemView.findViewById<ImageView>(R.id.image)
        val like = itemView.findViewById<RelativeLayout>(R.id.like_layout)
        val comment = itemView.findViewById<RelativeLayout>(R.id.comment_layout)
        val share = itemView.findViewById<RelativeLayout>(R.id.share_layout)


    }
}