package com.bignerdranch.android.bookz.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookz.BookFragment
import com.bignerdranch.android.bookz.BookListFragment
import com.bignerdranch.android.bookz.R
import java.util.*

class HomeFragment : Fragment(), BookListFragment.Callbacks {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val fragment = BookListFragment.newInstance()
        childFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()

        return root
    }

    override fun onBookSelected(bookId: UUID) {
        Log.d("F1", "On Home Fragment")
        val fragment = BookFragment.newInstance(bookId)
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()

    }
}