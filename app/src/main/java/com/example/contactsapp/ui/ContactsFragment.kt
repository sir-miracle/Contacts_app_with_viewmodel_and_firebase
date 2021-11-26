package com.example.contactsapp.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.R
import com.example.contactsapp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val adapter = ContactsRecyclerViewAdapter()
    private lateinit var viewModel: ContactViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactsRecyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "")
        }


        viewModel.contact.observe(viewLifecycleOwner, Observer {
            adapter.addContact(it)
        })

        viewModel.getRealtimeUpdate()

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.contactsRecyclerView)
    }

    private var simpleCallback = object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or (ItemTouchHelper.RIGHT)) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val currentContact = adapter.contacts[position]
            //The when detects when the recycler view is swiped left or right, and calls the UpdateContactDialogfragment class or deletes the contact accordingly
            when (direction) {
                ItemTouchHelper.RIGHT -> {
                    UpdateContactDialogFragment(currentContact).show(childFragmentManager, "")
                }
                ItemTouchHelper.LEFT -> {
                    AlertDialog.Builder(requireContext()).also {
                        it.setTitle("Sure to delete this contact?")
                        it.setPositiveButton("Yes Sure") { Dialog, which ->
                            viewModel.deleteContact(currentContact)
                            binding.contactsRecyclerView.adapter?.notifyItemRemoved(position)
                            Toast.makeText(
                                context,
                                "Contact deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.create().show()
                }
            }

            binding.contactsRecyclerView.adapter?.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}