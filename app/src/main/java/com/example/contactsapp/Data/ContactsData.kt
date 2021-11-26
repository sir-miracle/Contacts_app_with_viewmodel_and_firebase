package com.example.contactsapp.Data

import com.google.firebase.database.Exclude

data class ContactsData (
    @get:Exclude
    var id:String? = null,
    var fullName: String? =null,
    var phoneNumber:String? = null,

    @get:Exclude
    var isDeleted: Boolean = false){

    override fun equals(other: Any?): Boolean {
        return if(other is ContactsData){
            other.id == id
        }else{
            return false
        }
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber?.hashCode() ?: 0)
        result = 31 * result + isDeleted.hashCode()
        return result
    }

}