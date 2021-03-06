package com.bignerdranch.android.bookz

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookz.Adapter.UserAdapter
import com.bignerdranch.android.bookz.ModelClasses.Users
import com.bignerdranch.android.bookz.Repo.Repo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import java.util.*

private const val TAG = "BookFragment"
private const val ARG_BOOK_ID = "book_id"
class BookDetailFragment : Fragment() {
    private lateinit var book: Post

    //edit text fields
    private lateinit var titleField: TextView
    private lateinit var authorField: TextView
    private lateinit var priceField: TextView
    private lateinit var isbnField: TextView
    private lateinit var detailsField: TextView

    private lateinit var submitButton: Button

    private lateinit var conditionBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val bookId: UUID = arguments?.getSerializable(ARG_BOOK_ID) as UUID

        Log.d("F1", "book title: ${book.bookTitle}")

        // Eventually, load crime from database
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container,
            false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleField = view.findViewById(R.id.book_detail_title) as TextView
        titleField.text = book.bookTitle

        authorField = view.findViewById(R.id.book_detail_author) as TextView
        authorField.text = book.bookAuthor

        priceField = view.findViewById(R.id.book_detail_price) as TextView
        priceField.text = book.bookPrice

        isbnField = view.findViewById(R.id.book_detail_ISBN) as TextView
        isbnField.text = book.bookISBN

        detailsField = view.findViewById(R.id.book_detail_details) as TextView
        detailsField.text = book.bookDescription

        submitButton = view.findViewById(R.id.contact_buyer_button) as Button
        submitButton.apply {
            text ="Contact this seller"
            isEnabled = true
        }
        conditionBox = view.findViewById(R.id.book_detail_condition) as CheckBox

        submitButton.setOnClickListener {view: View->
            var ua: UserAdapter = UserAdapter(requireContext(), null, true)
            Log.d("F2", "Starting Chat on Fragment")
            Log.d("F2", "Owner Id: " + book.ownerID.toString())
            ua.startChatting(book.ownerID!!)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {

        fun newInstance(newBook: Post): BookDetailFragment {


            val args = Bundle().apply {
                //putSerializable(ARG_BOOK_ID, book)
            }
            val bf : BookDetailFragment = BookDetailFragment()
                //.apply{
               // arguments = args
            //}

            newBook.bookTitle?.let { Log.d("F1", it) }

            bf.book = newBook
            return bf
        }
    }
}