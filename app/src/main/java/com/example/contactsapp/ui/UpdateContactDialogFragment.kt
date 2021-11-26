package com.example.contactsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contactsapp.Data.ContactsData
import com.example.contactsapp.R
import com.example.contactsapp.databinding.FragmentAddContactDialogBinding
import com.example.contactsapp.databinding.FragmentUpdateContactDialogBinding
import java.util.EnumSet.of
import java.util.List.of

class UpdateContactDialogFragment (private val contact: ContactsData): DialogFragment() {
private var _binding: FragmentUpdateContactDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.editTextFullName.setText(contact.fullName)
        binding.editTextPhoneNumber.setText(contact.phoneNumber)


        binding.btnUpdate.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextPhoneNumber.text.toString()

            if(fullName.isEmpty()){
                binding.editTextFullName.error = "This field is required"
                return@setOnClickListener
            }

            if(contactNumber.isEmpty()){
                binding.editTextPhoneNumber.error = "This field is required"
                return@setOnClickListener
            }


            contact.fullName = fullName
            contact.phoneNumber = contactNumber

            viewModel.updateContact(contact)
            dismiss()
            Toast.makeText(context, "This contact has been updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
}