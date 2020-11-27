package com.bignerdranch.android.bookz

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

private val TAG = "ForgotPasswordFragment"
//UI elements
private var etEmail: EditText? = null
private var btnSubmit: Button? = null
//Firebase references
private var mAuth: FirebaseAuth? = null
private var db: Database? = null
class ForgotPasswordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        etEmail = view.findViewById<View>(R.id.et_email) as EditText
        btnSubmit = view.findViewById<View>(R.id.btn_submit) as Button
        mAuth = FirebaseAuth.getInstance()
        db = Database()
        btnSubmit!!.setOnClickListener { db!!.sendPasswordResetEmail(requireActivity(),User(null,null, etEmail?.text.toString(),null,null)) }
        return view
    }
    /*
    private fun initialise() {
        etEmail = findViewById<View>(R.id.et_email) as EditText
        btnSubmit = findViewById<View>(R.id.btn_submit) as Button
        mAuth = FirebaseAuth.getInstance()
        btnSubmit!!.setOnClickListener { sendPasswordResetEmail() }
    }
     */
    /*
    private fun sendPasswordResetEmail() {
        val email = etEmail?.text.toString()
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val message = "Email sent."
                        Log.d(TAG, message)
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        updateUI()
                    } else {
                        task.exception!!.message?.let { Log.w(TAG, it) }
                        Toast.makeText(activity, "No user found with this email.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_SHORT).show()
        }
    }
    */

    private fun updateUI() {
        val intent = Intent(activity, LoginFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}