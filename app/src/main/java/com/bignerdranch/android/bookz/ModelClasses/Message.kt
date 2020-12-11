package com.bignerdranch.android.bookz.ModelClasses

class Message {
    private var message: String = ""
    private var sender: String = ""
    private var receiver: String = ""
    private var messageId: String = ""
    private var seen = false
    private var url: String = ""

    constructor()
    constructor(sender: String, message: String, receiver: String, seen: Boolean, url: String, messageId: String) {
        this.message = message
        this.sender = sender
        this.receiver = receiver
        this.seen = seen
        this.url = url
        this.messageId = messageId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message!!
    }

    fun getSender(): String? {
        return sender
    }

    fun setSender(sender: String?) {
        this.sender = sender!!
    }


    fun getReceiver(): String? {
        return receiver
    }

    fun setReceiver(receiver: String?) {
        this.receiver = receiver!!
    }

    fun getMessageId(): String? {
        return messageId
    }

    fun setMessageId(messageId: String?) {
        this.messageId = messageId!!
    }

    fun isSeen(): Boolean {
        return seen
    }

    fun setSeen(seen: Boolean?) {
        this.seen = seen!!
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url!!
    }


}