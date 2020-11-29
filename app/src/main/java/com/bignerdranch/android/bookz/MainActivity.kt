package com.bignerdranch.android.bookz

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.android.gms.tasks.Continuation
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val RequestCode = 438;
    private var imageUri: Uri? = null
    private var storageRef: StorageReference? = null
    var firebaseUser: FirebaseUser? = null
    var userReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_messages, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /*firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser?.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("Users")*/
    }

    fun logoutUser(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    fun changePicture(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null ) {
            imageUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            profile_picture.setBackgroundDrawable(bitmapDrawable)

            uploadImageToFirebase()
        }
    }

    private fun uploadImageToFirebase() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(imageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                }
            }
    }

}
