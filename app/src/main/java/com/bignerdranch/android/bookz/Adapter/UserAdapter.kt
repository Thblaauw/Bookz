package com.bignerdranch.android.bookz.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.MessageActivity
import com.bignerdranch.android.bookz.ModelClasses.Message
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(
    context: Context,
    users: List<Users>?,
    checkMessages: Boolean
    ) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val context: Context
    private lateinit var users: List<Users>
    private var checkChat: Boolean
    var lastMessage: String = ""

    init {
        if (users != null) {
            this.users = users
        }
        this.context = context
        this.checkChat = checkMessages
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.search_user, viewGroup, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user: Users = users[i]
        holder.fullnameText.text = user!!.getFirstName() + " " + user!!.getLastName()
        Picasso.get().load(user.getProfilePicture()).into(holder.profileImageView)

        if(checkChat) {
            getLastMessage(user.getUID(),holder.lastMessageText)
        }
        else {
            holder.lastMessageText.visibility = View.GONE
        }
        

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence> (
                "Send Message"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra("visit_id", user.getUID())
                context.startActivity(intent)
            })
            builder.show()
        }
    }

    private fun getLastMessage(chatUserId: String?, lastMessageText: TextView) {
        lastMessage = "defaultMsg"
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children) {
                    val message: Message? = dataSnapshot.getValue(Message::class.java)
                    if (firebaseUser != null && message != null) {
                        if (message.getReceiver() == firebaseUser!!.uid && message.getSender() == chatUserId || message.getReceiver() == chatUserId && message.getSender() == firebaseUser!!.uid) {
                            lastMessage = message.getMessage()!!
                        }
                    }
                }

                when (lastMessage) {
                    "defaultMsg" -> lastMessageText.text = "No Message"
                    "Attatchment: Image" -> lastMessageText.text = "Attatchment: Image"
                    else -> lastMessageText.text = lastMessage
                }
                lastMessage = "defaultMsg"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    override fun getItemCount(): Int {
        return users.size
    }

    fun startChatting(id: String){
        Log.d("F2", "Starting Chat on UserAdapter")
        val intent = Intent(context, MessageActivity::class.java)
        intent.putExtra("visit_id", id)
        context.startActivity(intent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullnameText: TextView
        var profileImageView: CircleImageView
        var lastMessageText: TextView

        init {
            fullnameText = itemView.findViewById(R.id.fullname)
            profileImageView = itemView.findViewById(R.id.profile_pic_bar)
            lastMessageText = itemView.findViewById(R.id.message_last)
        }

    }

}