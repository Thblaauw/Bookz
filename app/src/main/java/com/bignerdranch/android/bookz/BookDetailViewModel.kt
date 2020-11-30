package com.bignerdranch.android.bookz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class BookDetailViewModel() : ViewModel() {
    /*
    private val bookRepository = BookRepository.get()
    private val bookIdLiveData = MutableLiveData<UUID>()
    var bookLiveData: LiveData<Book?> =
        Transformations.switchMap(bookIdLiveData) { bookId ->
            bookRepository.getBook(bookId)
        }
    fun loadBook(bookId: UUID) {
        bookIdLiveData.value = bookId
    }

     */
}