package com.bignerdranch.android.bookz.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.Adapter.UserAdapter
import com.bignerdranch.android.bookz.ModelClasses.MessageList
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessagesFragment : Fragment() {

    private lateinit var messagesViewModel: MessagesViewModel
    private var userAdapter: UserAdapter? = null
    private var users: List<Users>? = null
    private var usersMessageList: List<MessageList>? = null
    lateinit var recyclerview_messages: RecyclerView
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        messagesViewModel =
            ViewModelProviders.of(this).get(MessagesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        recyclerview_messages = root.findViewById(R.id.recyclerview_messages)
        recyclerview_messages.setHasFixedSize(true)
        recyclerview_messages.layoutManager = LinearLayoutManager(context)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersMessageList = ArrayList()
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (usersMessageList as ArrayList).clear()
                for (dataSnapshot in p0.children) {
                    (usersMessageList as ArrayList).add(dataSnapshot.getValue(MessageList::class.java)!!)
                }
                getMessageList()
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return root
    }

    private fun getMessageList(){
        users = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (users as ArrayList).clear()
                for (dataSnapshot in p0.children) {
                    val user = dataSnapshot.getValue(Users::class.java)
                    for (eachChatList in usersMessageList!!) {
                        if (user!!.getUID().equals(eachChatList.getId())) {
                            (users as ArrayList).add(user!!)
                        }
                    }
                }
                userAdapter = UserAdapter(context!!, (users as ArrayList<Users>), true)
                recyclerview_messages.adapter = userAdapter

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

