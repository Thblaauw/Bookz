package com.bignerdranch.android.bookz

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class PostEntity(
    var postID: String?, var bookTitle: String?, var bookImage: String?,
    var bookDescription: String?, var bookPrice: String?, var bookAuthor: String?,
    var bookISBN: String?, var bookCondition: Boolean?
)