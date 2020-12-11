package com.bignerdranch.android.bookz

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Adapter.MessageAdapter
import com.bignerdranch.android.bookz.ModelClasses.Message
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_message_chat.*
import com.google.android.gms.tasks.Continuation
import com.google.firebase.database.*

class MessageActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var messageAdapter: MessageAdapter? = null
    var messageList: List<Message>? = null
    lateinit var recyclerview_messages: RecyclerView
    var reference: DatabaseReference? = null
    var userIdVisit: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)

        intent = intent
        userIdVisit = intent.getStringExtra("visit_id").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser

        recyclerview_messages = findViewById(R.id.recyclerview_messages)
        recyclerview_messages.setHasFixedSize(true)
        var linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerview_messages.layoutManager = linearLayoutManager

        val uid = FirebaseAuth.getInstance().uid


        reference = FirebaseDatabase.getInstance().reference.child("Users/").child(userIdVisit)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user: Users? = p0.getValue(Users::class.java)

                fullname_message.text = user!!.getFirstName() + " " + user!!.getLastName()

                getMessages(firebaseUser!!.uid, userIdVisit, user!!.getProfilePicture())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btn_send_message.setOnClickListener {
            val message = text_message.text.toString()
            if(message == "" ) {
                Toast.makeText(this@MessageActivity, "Type a message", Toast.LENGTH_LONG).show()
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
        messageHashMap["message"] = message
        messageHashMap["sender"] = senderId
        messageHashMap["receiver"] = receiverId
        messageHashMap["messageId"] = messageKey
        messageHashMap["seen"] = false
        messageHashMap["url"] = ""
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
                    messageHashMap["message"] = "Attachment: Image"
                    messageHashMap["sender"] = firebaseUser!!.uid
                    messageHashMap["receiver"] = userIdVisit
                    messageHashMap["messageId"] = messageId
                    messageHashMap["seen"] = false
                    messageHashMap["url"] = url

                    ref.child("Chats").child(messageId!!).setValue(messageHashMap)

                    loadingBar.dismiss()
                }
            } 

        }
    }

    private fun getMessages(senderId: String, receiverId: String?, recieverImageUrl: String?) {
        messageList = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (messageList as ArrayList<Message>).clear()
                for (snapshot in p0.children) {
                    val chat = snapshot.getValue(Message::class.java)

                    if (chat!!.getReceiver().equals(senderId) && chat.getSender().equals(receiverId) || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)) {
                        (messageList as ArrayList<Message>).add(chat)
                }
                    messageAdapter = MessageAdapter(this@MessageActivity,(messageList as ArrayList<Message>), recieverImageUrl!!)
                    recyclerview_messages.adapter = messageAdapter

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
                    val chat = dataSnapshot.getValue(Message::class.java)
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