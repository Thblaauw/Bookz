package com.bignerdranch.android.bookz.Repo

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Post
import com.google.firebase.auth.FirebaseAuth
//import com.bignerdranch.android.bookz.User

import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Repo{
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()

    fun getBook(): LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        FirebaseDatabase.getInstance().getReference("Posts").addListenerForSingleValueEvent(object:   ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Post>()
                for(datas: DataSnapshot in snapshot.children) {
                    val id = datas.child("postID").value.toString()
                    val title = datas.child("bookTitle").value.toString()
                    val author = datas.child("bookAuthor").value.toString()
                    val price =datas.child("bookPrice").value.toString()
                    val image = datas.child("bookImage").value.toString()
                    val description = datas.child("bookDescription").value.toString()
                    val ISBN = datas.child("bookISBN").value.toString()
                    val condition = datas.child("bookCondition").value.toString().toBoolean()
                    val ownerId = datas.child("ownerID").value.toString()

                    val book = Post(id,title,image,description,price,author,ISBN,condition, ownerId)
                    listData.add(book)
                }
                mutableData.value=listData

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return mutableData
    }

    fun addBook(context: Context, book: Post) {
        //add the ownerID in this line
        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser
        book.ownerID = currentUser?.uid

        mDatabaseReference = mDatabase?.getReference("Posts")
        val bookID = mDatabaseReference!!.push().key
        if (bookID != null) {
            book.postID = bookID
            mDatabaseReference!!.child(bookID).setValue(book).addOnCompleteListener {
                Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()
            }
        }

    }
   
    fun searchByTitle(title: String):LiveData<MutableList<Post>>{
        val mutableData = MutableLiveData<MutableList<Post>>()
        FirebaseDatabase.getInstance().getReference("Posts").orderByChild("bookTitle")
            .startAt(title)
            .endAt(title+ "\ufbff").addListenerForSingleValueEvent(object:   ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Post>()
                for(datas: DataSnapshot in snapshot.children) {
                    val id = datas.child("postID").value.toString()
                    val title = datas.child("bookTitle").value.toString()
                    val author = datas.child("bookAuthor").value.toString()
                    val price =datas.child("bookPrice").value.toString()
                    val image = datas.child("bookImage").value.toString()
                    val description = datas.child("bookDescription").value.toString()
                    val ISBN = datas.child("bookISBN").value.toString()
                    val condition = datas.child("bookCondition").value.toString().toBoolean()
                    val ownerId = datas.child("ownerID").value.toString()

                    val book = Post(id,title,image,description,price,author,ISBN,condition, ownerId)
                    listData.add(book)
                }
                mutableData.value=listData

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return mutableData
    }
}
