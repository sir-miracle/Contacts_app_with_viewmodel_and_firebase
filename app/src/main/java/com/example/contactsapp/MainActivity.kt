package com.example.contactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.example.contactsapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var dataBase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name = findViewById<EditText>(R.id.your_name)
        val number = findViewById<EditText>(R.id.your_phone_number)
        val btn = findViewById<Button>(R.id.save)

        btn.setOnClickListener {
        val inputName = name.text.toString()
            val inputPhoneNumber = number.text.toString()

            dataBase = FirebaseDatabase.getInstance().getReference("contacts")
            val contactsId = dataBase.push().key

            val contacts = Contacts(contactsId, inputName, inputPhoneNumber)

            if (contactsId != null) {
                dataBase.child(contactsId).setValue(contacts).addOnSuccessListener {
                    name.text.clear()
                    number.text.clear()
                   Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                   Toast.makeText(this, "Failed to save your contact", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}