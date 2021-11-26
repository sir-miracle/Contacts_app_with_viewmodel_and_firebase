package com.example.contactsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contactsapp.Data.ContactsData
import com.example.contactsapp.R
import com.example.contactsapp.databinding.FragmentAddContactDialogBinding
import java.util.EnumSet.of
import java.util.List.of
import java.util.regex.Pattern

class AddContactDialogFragment : DialogFragment() {
private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if(it==null ){
                getString(R.string.added_contact)
            }
            else{
                getString(R.string.error, it.message )
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        binding.btnSave.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextPhoneNumber.text.toString()

            //........
           // var pattern: Pattern = Pattern.compile(^[+]*[(]{0;1}[0-9]{1;4}[)]{0;1}[-\s\./0-9]*$))

            //........

            if(fullName.isEmpty()){
                binding.editTextFullName.error = "This field is required"
                return@setOnClickListener
            }

//            if (!contactNumber.isDigitsOnly()){
//                binding.editTextPhoneNumber.error = "Not expecting characters or alphabets"
//                return@setOnClickListener
//            }

            if(contactNumber.isEmpty()){
                binding.editTextPhoneNumber.error = "This field is required"
                return@setOnClickListener
            }

            if(!contactNumber.isDigitsOnly()){
                binding.editTextPhoneNumber.error = "Number must be digits only"
                return@setOnClickListener
            }

            val contact = ContactsData()
            contact.fullName = fullName
            contact.phoneNumber = contactNumber

            viewModel.addContact(contact)
        }
    }
}