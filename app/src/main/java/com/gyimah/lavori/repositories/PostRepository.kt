package com.gyimah.lavori.repositories

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.gyimah.lavori.listeners.PostListener
import com.gyimah.lavori.models.Post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
){

    private lateinit var listener: PostListener

    fun setPostListener(listener: PostListener) {
        this.listener = listener
    }

    fun getPosts() {
        val posts = mutableListOf<Post>()
        firebaseDatabase.reference.child("posts")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    posts.clear()

                    for (item in snapshot.children) {

                        val post = snapshot.getValue<Post>()

                        if (post != null) {
                            posts.add(post)
                        }

                    }

                    listener.onPostsRetrieved(posts)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("POST", "error fetching posts: "+error.message)

                    listener.onPostsError(error.message)
                }

            })
    }
}