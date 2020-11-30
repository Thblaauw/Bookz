package com.bignerdranch.android.bookz

import java.util.*

data class Book(val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var description: String = "",
                 var price: String = "",
                 var author: String = "",
                 var ISBN: String  =  "",
                 var condition: Boolean = false)
