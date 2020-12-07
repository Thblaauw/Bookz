package com.bignerdranch.android.bookz.ModelClasses

class ProfilePic {
    private var profilePicture: String = ""

    constructor()

    constructor(profilePicture: String) {
        this.profilePicture = profilePicture
    }

    fun getProfilePicture(): String?{
        return profilePicture
    }

    fun setProfilePicture(profilePicture: String){
        this.profilePicture = profilePicture
    }
}
