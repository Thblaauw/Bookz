package com.bignerdranch.android.bookz.Repo

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Post

import com.google.firebase.database.*

class Repo{
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    fun getBook(): LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        FirebaseDatabase.getInstance().getReference("Posts").addListenerForSingleValueEvent(object:   ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Post>()
                for(datas: DataSnapshot in snapshot.children) {

                    val title = datas.child("bookTitle").value.toString()
                    val author = datas.child("bookAuthor").value.toString()
                    val price =datas.child("bookPrice").value.toString()



                    val book = Post(null,title,null,null,price,author,null,null)
                    listData.add(book)
                }
                mutableData.value=listData
                Log.d("data", mutableData.value!!.get(0).bookTitle.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        Log.d("sdasdawda", "onDataChange: Thread=" + Thread.currentThread().getName())
        return mutableData
    }

    fun addBook(context: Context, book: Post) {
        if (!TextUtils.isEmpty(book.postID) && !TextUtils.isEmpty(book.bookTitle)
            && !TextUtils.isEmpty(book.bookPrice) && !TextUtils.isEmpty(book.bookImage) && !TextUtils.isEmpty(
                book.bookISBN
            ) && !TextUtils.isEmpty(book.bookDescription) && !TextUtils.isEmpty(book.bookCondition.toString()) && !TextUtils.isEmpty(
                book.bookAuthor
            )
        ) {

            mDatabaseReference = mDatabase?.getReference("Posts")
            val bookID = mDatabaseReference!!.push().key
            if (bookID != null) {
                book.postID = bookID
                mDatabaseReference!!.child(bookID).setValue(book).addOnCompleteListener {
                    Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}