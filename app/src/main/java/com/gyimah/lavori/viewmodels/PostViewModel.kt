package com.gyimah.lavori.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gyimah.lavori.listeners.PostListener
import com.gyimah.lavori.models.Post
import com.gyimah.lavori.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel(), PostListener {

    init {
        postRepository.setPostListener(this)
    }

    val postState = MutableLiveData<List<Post>>()
    val errorMessageState = MutableLiveData<String>()


    fun getPosts() {
        postRepository.getPosts()
    }

    override fun onPostsRetrieved(posts: MutableList<Post>) {
        postState.postValue(posts)
    }

    override fun onPostsError(message: String) {
        errorMessageState.postValue(message)
    }
}