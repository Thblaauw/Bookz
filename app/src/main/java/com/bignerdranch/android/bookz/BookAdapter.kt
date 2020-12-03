package com.bignerdranch.android.bookz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_post_list.view.*
/*
class BookAdapter(private val context: Context): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var dataList = mutableListOf<Post>()
    fun setListData(data: MutableList<Post>) {
        dataList = data
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(book: Post) {
            //pic Glide.with(context).load(url).into
            itemView.book_title.text=book.bookTitle
            itemView.book_price.text=book.bookPrice
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_book_list, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book: Post = dataList[position]
        holder.bindView(book)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }


}

 */
