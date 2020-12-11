package com.bignerdranch.android.bookz

import android.util.Log
import android.widget.Toast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.bookz.Repo.Repo

class BookListViewModel : ViewModel() {
    private val repo = Repo()
    fun fetchBooks(): LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        repo.getBook().observeForever{postList->
            mutableData.value = postList

        }

        return mutableData
    }
    fun searchBooksByTitle(title: String): LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        repo.searchByTitle(title).observeForever{postList->
            mutableData.value = postList
        }

        return mutableData
    }

}
