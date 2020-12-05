package com.bignerdranch.android.bookz.Repo

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Post
import com.bignerdranch.android.bookz.User

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
                    val id = datas.child("postID").value.toString()
                    val title = datas.child("bookTitle").value.toString()
                    val author = datas.child("bookAuthor").value.toString()
                    val price =datas.child("bookPrice").value.toString()
                    val image = datas.child("bookImage").value.toString()
                    val description = datas.child("bookDescription").value.toString()
                    val ISBN = datas.child("bookISBN").value.toString()
                    val condition = datas.child("bookCondition").value.toString().toBoolean()

                    val book = Post(id,title,image,description,price,author,ISBN,condition)
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

    fun getUser(): LiveData<MutableList<User>>{
        val mutableData = MutableLiveData<MutableList<User>>()
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(object:   ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<User>()
                for(datas: DataSnapshot in snapshot.children) {
                    val fn = datas.child("firstName").value.toString()
                    val ln = datas.child("lastName").value.toString()
                    val email = datas.child("email").value.toString()
                    val pw =datas.child("password").value.toString()
                    val sc =datas.child("school").value.toString()
                    val pic =datas.child("profilePicture").value.toString()



                    val user = User(fn,ln,email,pw,sc,pic)
                    listData.add(user)
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