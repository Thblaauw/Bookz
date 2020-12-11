package com.bignerdranch.android.bookz.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.bookz.ModelClasses.Message
import com.bignerdranch.android.bookz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    context: Context,
    messageList: List<Message>,
    imageUrl: String
    ): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val context: Context
    private val messageList: List<Message>
    private val imageUrl: String
    var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    init {
        this.context = context
        this.messageList = messageList
        this.imageUrl = imageUrl
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return if (position == 1) {
            val view: View = LayoutInflater.from(context).inflate(com.bignerdranch.android.bookz.R.layout.item_right_message, parent, false)
            ViewHolder(view)
        }

        else {
            val view: View = LayoutInflater.from(context).inflate(com.bignerdranch.android.bookz.R.layout.item_left_message, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: Message  = messageList[position]

        if(message.getMessage().equals("Attachment: Image") && !message.getUrl().equals("")) {
            if(message.getSender().equals(firebaseUser!!.uid)) {
                holder.show_message!!.visibility = View.GONE
                holder.imageview_right!!.visibility = View.VISIBLE
                Picasso.get().load(message.getUrl()).into(holder.imageview_right)
            }

            else if(!message.getSender().equals(firebaseUser!!.uid)) {
                holder.show_message!!.visibility = View.GONE
                holder.imageview_left!!.visibility = View.VISIBLE
                Picasso.get().load(message.getUrl()).into(holder.imageview_left)
            }
        }

        else {
            holder.show_message!!.text = message.getMessage()
        }

        if(position == messageList.size-1) {
            if(message.isSeen()) {
                holder.message_seen!!.text = "Seen"
                if(message.getMessage().equals("Attachment: Image") && !message.getUrl().equals("")) {
                    val lp: RelativeLayout.LayoutParams? = holder.message_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0,245,10,0)
                    holder.message_seen!!.layoutParams = lp

                }
            }
            else {
                holder.message_seen!!.text = "Sent"
                if(message.getMessage().equals("Attachment: Image") && !message.getUrl().equals("")) {
                    val lp: RelativeLayout.LayoutParams? = holder.message_seen!!.layoutParams as RelativeLayout.LayoutParams?
                    lp!!.setMargins(0,245,10,0)
                    holder.message_seen!!.layoutParams = lp

                }
            }
        }
        else {
            holder.message_seen!!.visibility = View.GONE

        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profile_image_left: CircleImageView? = null
        var show_message: TextView? = null
        var imageview_left: ImageView? = null
        var message_seen: TextView? = null
        var imageview_right: ImageView? = null


        init {
            profile_image_left = itemView.findViewById(R.id.profile_image_left)
            show_message = itemView.findViewById(R.id.show_message)
            imageview_left = itemView.findViewById(R.id.imageview_left)
            message_seen = itemView.findViewById(R.id.message_seen)
            imageview_right = itemView.findViewById(R.id.imageview_right)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].getSender().equals(firebaseUser!!.uid)) {
            1
        }
        else {
            0
        }
    }


}