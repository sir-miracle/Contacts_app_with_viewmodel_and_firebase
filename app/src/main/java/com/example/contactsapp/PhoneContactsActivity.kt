package com.example.contactsapp

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class PhoneContactsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_contacts)

        if (requestPermissions()) {
            readContacts()
        } else {
            //requestPermissions()
        }
    }

    private fun isContactsAccessGranted() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestPermissions(): Boolean {
        var granted: Boolean = false
        val permissionsToRequest = mutableListOf<String>()

        if (!isContactsAccessGranted()) { // if permission is not granted already, add it to the list 'permissions request'
            permissionsToRequest.add(Manifest.permission.READ_CONTACTS)
        }
        //This if statement checks if the list is empty or not
        //If empty, it means the permission was granted
        if (permissionsToRequest.isNotEmpty()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                AlertDialog.Builder(this).setTitle(getString(R.string.permission_needed))
                    .setMessage("For the app to read your contacts, you have to give the permission to do so")
                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            permissionsToRequest.toTypedArray(),
                            0
                        )
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }).create().show()

            } else {
                ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
                // granted = false
            }

        } else {
            //granted = true
            readContacts()
        }

        return granted
    }
    // we have to call the onpermissionRequestResult here to always know whether permission granted or not

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    readContacts()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // this function reads the phone contacts and loads them into a list view
    @RequiresApi(Build.VERSION_CODES.O)
    fun readContacts() {
        var cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null
        )
        startManagingCursor(cursor)

        var from = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID
        )
        var to = intArrayOf(android.R.id.text1, android.R.id.text2)

        var listAdapter =
            SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to)

        var contactsList = findViewById<ListView>(R.id.contacts_list)
        contactsList.adapter = listAdapter
    }
}