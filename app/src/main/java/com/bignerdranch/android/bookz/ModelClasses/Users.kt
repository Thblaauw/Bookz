package com.bignerdranch.android.bookz.ModelClasses

import com.google.firebase.database.DataSnapshot

class Users {
    private var uid: String = ""
    private var profilePicture: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var search: String = ""
    private var schoolName: String = ""

    constructor()

    constructor(
        uid: String,
        profilePicture: String,
        firstName: String,
        lastName: String,
        search: String,
        schoolName: String
    ) {
        this.uid = uid
        this.profilePicture = profilePicture
        this.firstName = firstName
        this.lastName = lastName
        this.search = search
        this.schoolName = schoolName
    }

    fun getSearch(): String?{
        return search
    }

    fun setSearch(search: String) {
        this.search = search
    }


    fun getUID(): String?{
        return uid
    }

    fun setUID(uid: String){
        this.uid = uid
    }

    fun getProfilePicture(): String?{
        return profilePicture
    }

    fun setProfilePicture(profilePicture: String){
        this.profilePicture = profilePicture
    }

    fun getFirstName(): String?{
        return firstName
    }

    fun setFirstName(firstName: String){
        this.firstName = firstName
    }

    fun getLastName(): String?{
        return lastName
    }

    fun setLastName(lastName: String){
        this.lastName = lastName
    }

    fun getSchoolName(): String?{
        return schoolName
    }

    fun setSchoolName(schoolName: String){
        this.schoolName = schoolName
    }

}