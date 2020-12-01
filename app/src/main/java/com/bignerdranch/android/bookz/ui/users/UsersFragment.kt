package com.bignerdranch.android.bookz.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.bignerdranch.android.bookz.R
import com.bignerdranch.android.bookz.ui.users.UsersViewModel


class UsersFragment : Fragment() {

    private lateinit var usersViewModel: UsersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usersViewModel =
            ViewModelProviders.of(this).get(UsersViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_users, container, false)
        return root
    }
}