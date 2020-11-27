package com.bignerdranch.android.bookz

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private var mProgressBar: ProgressDialog? = null
private var mDatabaseReference: DatabaseReference? = null
private var mDatabase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null

class Database
{
    init {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
    }
    fun addUser(context: Context, user: User){
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        if (!TextUtils.isEmpty(user.firstName) && !TextUtils.isEmpty(user.lastName)
            && !TextUtils.isEmpty(user.email) && !TextUtils.isEmpty(user.password)&& !TextUtils.isEmpty(
                user.school
            )
        ) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
        } else {
            Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show()
        }
        mAuth!!
            .createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener() { task ->
                mProgressBar!!.hide()
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("CreateAccountFragment", "createUserWithEmail:success")
                    val userId = mAuth!!.currentUser!!.uid
                    //Verify Email
                    val mUser = mAuth!!.currentUser;
                    mUser!!.sendEmailVerification()
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,

                                    "Verification email sent to " + mUser.getEmail(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.e(
                                    "CreateAccountFragment",
                                    "sendEmailVerification",
                                    task.exception
                                )
                                Toast.makeText(
                                    context,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    //update user profile information
                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("firstName").setValue(user.firstName)
                    currentUserDb.child("lastName").setValue(user.lastName)
                    currentUserDb.child("schoolName").setValue(user.school)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreateAccountFragment", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    fun loginUser(context: Context, user: User) {

        if (!TextUtils.isEmpty(user.email) && !TextUtils.isEmpty(user.password)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
            Log.d("LoginFragment", "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener() { task ->
                    mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d("LoginFragment", "signInWithEmail:success")

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("LoginFragment", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
    fun sendPasswordResetEmail(context: Context, user: User){
        if (!TextUtils.isEmpty(user.email)) {
            user.email?.let {
                mAuth!!
                    .sendPasswordResetEmail(it)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val message = "Email sent."
                            Log.d("ForgotPasswordFragment", message)
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                        } else {
                            task.exception!!.message?.let { Log.w("ForgotPasswordFragment", it) }
                            Toast.makeText(
                                context,
                                "No user found with this email.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        } else {
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
        }
    }
    fun addBook(context: Context, book: Post) {
        if (!TextUtils.isEmpty(book.postID) && !TextUtils.isEmpty(book.bookTitle)
            && !TextUtils.isEmpty(book.bookPrice) && !TextUtils.isEmpty(book.bookImage) && !TextUtils.isEmpty(
                book.bookISBN
            ) && !TextUtils.isEmpty(book.bookDescription) && !TextUtils.isEmpty(book.bookCondition) && !TextUtils.isEmpty(
                book.bookAuthor
            )
        ) {

            mDatabaseReference = mDatabase?.getReference("posts")
            val bookID = mDatabaseReference!!.push().key
            if (bookID != null) {
                book.postID = bookID
                mDatabaseReference!!.child(bookID).setValue(book).addOnCompleteListener {
                    Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
    fun deleteBook(book: Post){
        book.postID?.let { mDatabase?.reference?.child("posts")?.child(it)?.removeValue() }
    }
    fun searchBookByTitle(title: String): Post? {
        mDatabaseReference = mDatabase?.reference
        var value: ValueEventListener
        var bk: Post? = null
        mDatabaseReference?.child("post")?.orderByChild("bookTitle")!!.equalTo(title).
        addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datas: DataSnapshot in snapshot.children) run {
                    var bk =Post(datas.key.toString(),null,null,null,null,null,null,null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            })
        return bk
    }
}
