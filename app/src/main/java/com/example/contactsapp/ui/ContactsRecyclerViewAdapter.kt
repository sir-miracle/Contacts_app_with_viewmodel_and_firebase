package com.example.contactsapp.ui

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.Data.ContactsData
import com.example.contactsapp.databinding.ContactsRecyclerViewBinding
import com.example.contactsapp.databinding.FragmentContactsBinding
import com.google.firebase.database.core.Context
import kotlin.coroutines.coroutineContext


class ContactsRecyclerViewAdapter : RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {
    //create a list that contains the recyclerview contents
    val contacts = mutableListOf<ContactsData>()

    inner class ViewHolder(val binding: ContactsRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val numberTextView = binding.numberTextView
        private val nameTextView = binding.nameTextView
        private val shareImageButton = binding.shareButton
        private val callImageButton = binding.callButton

        fun bind(contact: ContactsData) {
            nameTextView.text = contact.fullName
            numberTextView.text = contact.phoneNumber

            callImageButton.setOnClickListener {
                binding.apply {

                    val number = numberTextView.text.toString().trim()
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))

                    startActivity(root.context, intent, null)

                }
            }

            shareImageButton.setOnClickListener {
                val number = binding.numberTextView.text.toString().trim()
                val name = binding.nameTextView.text.toString().trim()
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$name \n $number")
                    type = "text/plain"
                }
                startActivity(binding.root.context, intent, null)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContactsRecyclerViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    // function to add new contacts to firebase
    fun addContact(contact: ContactsData) {
        if (!contacts.contains(contact)) {
            contacts.add(contact)
        } else {
            val index = contacts.indexOf(contact)

            if (contact.isDeleted) {
                contacts.removeAt(index)
            } else {
                contacts[index] = contact
            }

        }

        notifyDataSetChanged()

    }
}