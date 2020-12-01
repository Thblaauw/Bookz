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
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.R
import com.bignerdranch.android.bookz.R.id.*
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
        //Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.profileImageView)

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence> (
                "Send Message",
                "Visit Profile"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    val intent = Intent(mContext, MessageChatActivity::class.java)
                    intent.putExtra("visit_id", user.getUID())
                    mContext.startActivity(intent)
                }

                if (which == 1) {

                }
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

}