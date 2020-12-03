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
    /*val books = mutableListOf<Book>()

    private val mutableSearchTerm = MutableLiveData<String>()
    init {
        for (i in 0 until 100) {
            val book = Book()
            book.title = "Book #$i"
            book.author = "Author #$i"
            book.price = "$10.00 or trade"
            book.ISBN = "90883019283"
            book.description = "Cool Description #$i"
            book.condition = i % 2 == 0
            books += book
        }

        mutableSearchTerm.value = "Physics"

        /*
        Transformations.switchMap(mutableSearchTerm) {
                searchTerm -> flickrFetchr.searchPhotos(searchTerm)
        }
        */

    }

    fun addBook(book: Book) {
        //crimeRepository.addCrime(crime)
        //add to the database
    }

    fun fetchBooks(query: String = "") {
        //mutableSearchTerm.value = query
        Log.d("ViewModel", query)
    }

     */
}