package com.bignerdranch.android.bookz

data class Post(
    var postID: String?, var bookTitle: String?, var bookImage: String?,
    var bookDescription: String?, var bookPrice: String?, var bookAuthor: String?,
    var bookISBN: String?, var bookCondition: Boolean?,
    var ownerID: String?
)