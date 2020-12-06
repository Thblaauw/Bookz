package com.bignerdranch.android.bookz

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Adapter.ChatAdapter
import com.bignerdranch.android.bookz.ModelClasses.Chat
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_chat.*
import com.google.android.gms.tasks.Continuation
import com.google.firebase.database.*

class MessageChatActivity : AppCompatActivity() {

    var userIdVisit: String = ""
    var firebaseUser: FirebaseUser? = null
    var chatAdapter: ChatAdapter? = null
    var mChatList: List<Chat>? = null
    lateinit var recyclerview_chats: RecyclerView
    var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        /*val toolbar: Toolbar = findViewById(R.id.toolbar_message_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@MessageChatActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }*/

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerview_chats = findViewById(R.id.recyclerview_chats)
        recyclerview_chats.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerview_chats.layoutManager = linearLayoutManager

        val uid = FirebaseAuth.getInstance().uid


        reference = FirebaseDatabase.getInstance().reference.child("Users/").child(userIdVisit)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user: Users? = p0.getValue(Users::class.java)

                username_chat.text = user!!.getFirstName() + " " + user!!.getLastName()
                //Picasso.get().load(user.getProfilePicture()).into(profile_image_chat)

                retrieveMessages(firebaseUser!!.uid, userIdVisit, user!!.getProfilePicture())

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btn_send_message.setOnClickListener {
            val message = text_message.text.toString()
            if(message == "" ) {
                Toast.makeText(this@MessageChatActivity, "Type a message", Toast.LENGTH_LONG).show()
            }
            else {
                sendMessage(firebaseUser!!.uid, userIdVisit, message)
            }
            text_message.setText("")
        }

        btn_image_attachment.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent,"Select An Image"), 0)
        }
        seenMessage(userIdVisit)
    }

    private fun sendMessage(senderId: String, receiverId: String?, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key

        val messageHashMap = HashMap<String, Any?>()
        messageHashMap["sender"] = senderId
        messageHashMap["message"] = message
        messageHashMap["receiver"] = receiverId
        messageHashMap["seen"] = false
        messageHashMap["url"] = ""
        messageHashMap["messageId"] = messageKey
        reference.child("Chats").child(messageKey!!).setValue(messageHashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val chatListReference = FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid).child(userIdVisit)
                chatListReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        if (!p0.exists()) {
                            chatListReference.child("id").setValue(userIdVisit)
                        }

                        val chatListReceiverReference = FirebaseDatabase.getInstance().reference.child("ChatList").child(userIdVisit).child(firebaseUser!!.uid)
                        chatListReceiverReference.child("id").setValue(firebaseUser!!.uid)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

                val reference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==0 && resultCode==RESULT_OK && data!=null && data!!.data!=null) {
            val loadingBar = ProgressDialog(this)
            loadingBar.setMessage("Uploading Image...")
            loadingBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

            var uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["message"] = "Attachment: Image"
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["seen"] = false
                    messageHashMap["url"] = url
                    messageHashMap["messageId"] = messageId

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)

                    loadingBar.dismiss()
                }
            } 

        }
    }

    private fun retrieveMessages(senderId: String, receiverId: String?, recieverImageUrl: String?) {
        mChatList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()
                for (snapshot in p0.children) {
                    val chat = snapshot.getValue(Chat::class.java)

                    if (chat!!.getReceiver().equals(senderId) && chat.getSender().equals(receiverId) || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)) {
                        (mChatList as ArrayList<Chat>).add(chat)
                }
                    chatAdapter = ChatAdapter(this@MessageChatActivity,(mChatList as ArrayList<Chat>), recieverImageUrl!!)
                    recyclerview_chats.adapter = chatAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }
    var seenListener: ValueEventListener? = null
    private fun seenMessage(userId: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        seenListener = reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for(dataSnapshot in p0.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if(chat!!.getReceiver().equals(firebaseUser!!.uid) && chat!!.getSender().equals(userId)) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["seen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(seenListener!!)
    }
}