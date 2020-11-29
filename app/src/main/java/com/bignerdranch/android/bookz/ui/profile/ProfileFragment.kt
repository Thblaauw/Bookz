package com.bignerdranch.android.bookz.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var imageUri: Uri? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser
    var selectedPhotoUri: Uri?= null
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var refUsers: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
        })

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        loadProfile()

        return root
    }

    fun loadProfile() {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid/")
        val userInfo = Users()

        userreference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo.setFirstName(snapshot.child("firstName").value.toString())
                userInfo.setLastName(snapshot.child("lastName").value.toString())
                userInfo.setSchoolName(snapshot.child("schoolName").value.toString())
                FirebaseAuth.getInstance().uid?.let { userInfo.setUID(it) }
                userInfo.setProfilePicture(snapshot.child("profilePicture").value.toString())

                Picasso.get().load(userInfo.getProfilePicture()).into(profile_picture)

                text_full_name.text = userInfo.getFirstName() + " " + userInfo.getLastName()
                text_school.text = userInfo.getSchoolName()
                text_email.text = user?.email

                ref.setValue(userInfo)

            }

            override fun onCancelled(error: DatabaseError) {
                FirebaseAuth.getInstance().signOut()
            }
        })
    }


}



