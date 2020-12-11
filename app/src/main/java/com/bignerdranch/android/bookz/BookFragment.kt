package com.bignerdranch.android.bookz

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
import androidx.fragment.app.Fragment
import com.bignerdranch.android.bookz.Repo.Repo
import java.util.*

private const val TAG = "BookFragment"
private const val ARG_BOOK_ID = "book_id"
class BookFragment : Fragment() {

    private var firebase: Repo? = null
    private lateinit var book: Post


    //edit text fields
    private lateinit var titleField: EditText
    private lateinit var authorField: EditText
    private lateinit var priceField: EditText
    private lateinit var isbnField: EditText
    private lateinit var detailsField: EditText

    private lateinit var submitButton: Button

    private lateinit var conditionBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //val bookId: UUID = arguments?.getSerializable(ARG_BOOK_ID) as UUID
        //Log.d(TAG, "args bundle book ID: $bookId")
// Eventually, load crime from database
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book, container,
            false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleField = view.findViewById(R.id.book_title) as EditText
        authorField = view.findViewById(R.id.book_author) as EditText
        priceField = view.findViewById(R.id.book_price) as EditText
        isbnField = view.findViewById(R.id.book_ISBN) as EditText
        detailsField = view.findViewById(R.id.book_detiails) as EditText


        submitButton = view.findViewById(R.id.submit_button) as Button
        submitButton.apply {
            text ="Submit"
            isEnabled = true
        }

        submitButton.setOnClickListener {
            firebase?.addBook(requireActivity(), book)
        }

        conditionBox = view.findViewById(R.id.book_condition) as CheckBox

    }

    override fun onStart() {
        super.onStart()
        val genericTextWatcher = object : TextWatcher{
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                book.bookTitle = sequence.toString()

            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
                if(sequence.hashCode() == titleField.getText().hashCode())

                    book.bookTitle = sequence.toString()
                if(sequence.hashCode() == authorField.getText().hashCode())
                    book.bookAuthor = sequence.toString()
                if(sequence.hashCode() == priceField.getText().hashCode())
                    book.bookPrice = sequence.toString()
                if(sequence.hashCode() == isbnField.getText().hashCode())
                    book.bookISBN = sequence.toString()
                if(sequence.hashCode() == detailsField.getText().hashCode())
                    book.bookDescription = sequence.toString()

            }
        }
        titleField.addTextChangedListener(genericTextWatcher)
        authorField.addTextChangedListener(genericTextWatcher)
        priceField.addTextChangedListener(genericTextWatcher)
        isbnField.addTextChangedListener(genericTextWatcher)
        detailsField.addTextChangedListener(genericTextWatcher)

        conditionBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                book.bookCondition = isChecked

            }
        }
    }

    companion object {
        fun newInstance(): BookFragment {
            //val args = Bundle().apply {
                //putSerializable(ARG_BOOK_ID, bookId)
          //  }
            return BookFragment()//.apply {
                //arguments = args
            //}
        }
    }
}