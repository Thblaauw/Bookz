package com.bignerdranch.android.bookz

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), BookListFragment.Callbacks {
    private var imageUri: Uri? = null
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        /*
        if (currentFragment == null) {
            val fragment = BookListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
        */
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_messages, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")
        auth = FirebaseAuth.getInstance()
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

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            profile_picture.setImageBitmap(bitmap)

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
                    saveImageToDatabase(it.toString())
                }
            }
    }

    private fun saveImageToDatabase(profilePicture: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid/profilePicture")
        ref.setValue(profilePicture)
    }

    override fun onBookSelected(bookId: UUID) {
        /*
        val fragment = BookFragment.newInstance(bookId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

         */
        Log.d("F1", "On Main")
    }
}
