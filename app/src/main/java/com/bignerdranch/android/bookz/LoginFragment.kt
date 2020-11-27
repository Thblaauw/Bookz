package com.bignerdranch.android.bookz

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

private val TAG = "LoginFragment"
//global variables
private var email: String? = null
private var password: String? = null
//UI elements
private var tvForgotPassword: TextView? = null
private var etEmail: EditText? = null
private var etPassword: EditText? = null
private var btnLogin: Button? = null
private var btnCreateAccount: Button? = null
private var mProgressBar: ProgressDialog? = null
private var db: Database? = null
//Firebase references
private var mAuth: FirebaseAuth? = null
class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        tvForgotPassword = view.findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = view.findViewById<View>(R.id.et_email) as EditText
        etPassword = view.findViewById<View>(R.id.et_password) as EditText
        btnLogin = view.findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = view.findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = ProgressDialog(activity)
        mAuth = FirebaseAuth.getInstance()
        tvForgotPassword!!
            .setOnClickListener {
            }
        btnCreateAccount!!
            .setOnClickListener { }
        db = Database()
        btnLogin!!.setOnClickListener { db!!.loginUser(requireActivity(),User(null,null, etEmail?.text.toString(),etPassword?.text.toString(),null)) }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
   /* private fun initialise() {
        tvForgotPassword = findViewById<View>(R.id.tv_forgot_password) as TextView
        etEmail = findViewById<View>(R.id.et_email) as EditText
        etPassword = findViewById<View>(R.id.et_password) as EditText
        btnLogin = findViewById<View>(R.id.btn_login) as Button
        btnCreateAccount = findViewById<View>(R.id.btn_register_account) as Button
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        tvForgotPassword!!
            .setOnClickListener { startActivity(
                Intent(this@LoginActivity,
                ForgotPasswordActivity::class.java)
            ) }
        btnCreateAccount!!
            .setOnClickListener { startActivity(Intent(this@LoginActivity,
                CreateAccountActivity::class.java)) }
        btnLogin!!.setOnClickListener { loginUser() }
    }
    */
    /*
    private fun loginUser() {

        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener() { task ->
                    mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

     */
    /*
    private fun updateUI() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
*/
}