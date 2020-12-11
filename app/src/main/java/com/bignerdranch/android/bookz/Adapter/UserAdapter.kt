package com.bignerdranch.android.bookz.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.MessageChatActivity
import com.bignerdranch.android.bookz.ModelClasses.Chat
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.R
import com.bignerdranch.android.bookz.R.id.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    mContext: Context,
    mUsers: List<Users>,
    isChatCheck: Boolean
    ) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<Users>
    private var isChatCheck: Boolean
    var lastMsg: String = ""

    init {
        this.mUsers = mUsers
        this.mContext = mContext
        this.isChatCheck = isChatCheck
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.search_user, viewGroup, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user: Users = mUsers[i]
        holder.userNameText.text = user!!.getFirstName() + " " + user!!.getLastName()
        Picasso.get().load(user.getProfilePicture()).into(holder.profileImageView)

        if(isChatCheck) {
            getLastMessage(user.getUID(),holder.lastMessageText)
        }
        else {
            holder.lastMessageText.visibility = View.GONE
        }
        

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence> (
                "Send Message"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(mContext, MessageChatActivity::class.java)
                intent.putExtra("visit_id", user.getUID())
                mContext.startActivity(intent)
            })
            builder.show()
        }
    }


    override fun getItemCount(): Int {
        return mUsers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameText: TextView
        var profileImageView: CircleImageView
        var lastMessageText: TextView

        init {
            userNameText = itemView.findViewById(R.id.username)
            profileImageView = itemView.findViewById(R.id.profile_pic_bar)
            lastMessageText = itemView.findViewById(R.id.message_last)
        }

    }

    private fun getLastMessage(chatUserId: String?, lastMessageText: TextView) {
        lastMsg = "defaultMsg"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for(dataSnapshot in p0.children) {
                    val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                    if(firebaseUser!=null && chat!=null) {
                        if(chat.getReceiver() == firebaseUser!!.uid && chat.getSender() == chatUserId || chat.getReceiver() == chatUserId && chat.getSender() == firebaseUser!!.uid ) {
                            lastMsg = chat.getMessage()!!
                        }
                    }
                }

                when(lastMsg) {
                    "defaultMsg" -> lastMessageText.text = "No Message"
                    "Attatchment: Image" -> lastMessageText.text = "Attatchment: Image"
                    else -> lastMessageText.text = lastMsg
                }
                lastMsg = "defaultMsg"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

}