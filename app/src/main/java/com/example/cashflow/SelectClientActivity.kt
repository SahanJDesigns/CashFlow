package com.example.cashflow

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectClientActivity : AppCompatActivity() {
    private lateinit var contactsRecyclerView: RecyclerView
    private val contactsList = mutableListOf<User>()

    private val PERMISSIONS_REQUEST_READ_WRITE_CONTACTS = 100
    private val defaultCountryCode = "+94"
    private lateinit var contactsAdapter: ContactsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Check for permissions and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                PERMISSIONS_REQUEST_READ_WRITE_CONTACTS
            )
        } else {
            syncContacts()
        }

        contactsRecyclerView = findViewById(R.id.client_list)
        contactsAdapter = ContactsRecyclerAdapter(contactsList,this)
        contactsRecyclerView.layoutManager = LinearLayoutManager(this)
        contactsRecyclerView.adapter = contactsAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_WRITE_CONTACTS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                syncContacts()
            } else {
                Toast.makeText(this, "Permissions are required to read and write contacts.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun syncContacts() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val contentResolver: ContentResolver = contentResolver
                val cursor: Cursor? = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )

                cursor?.use {
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    while (it.moveToNext()) {
                        val contactName = it.getString(nameIndex)
                        var phoneNumber = it.getString(numberIndex)
                        val updatedNumber = addCountryCodeIfMissing(phoneNumber, defaultCountryCode)

                        val clientData = FirebaseFirestore.getInstance().collection("users").document(updatedNumber)
                        clientData.get().addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                contactsList.add(User(updatedNumber))
                                contactsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addCountryCodeIfMissing(phoneNumber: String, countryCode: String): String {
        // Remove all spaces from the phoneNumber
        val cleanedPhoneNumber = phoneNumber.replace(" ", "")

        // Remove leading '0' if present
        val trimmedPhoneNumber = if (cleanedPhoneNumber.startsWith("0")) {
            cleanedPhoneNumber.substring(1)
        } else {
            cleanedPhoneNumber
        }

        // Add countryCode if the number doesn't start with '+'
        return if (!trimmedPhoneNumber.startsWith("+")) {
            countryCode + trimmedPhoneNumber
        } else {
            trimmedPhoneNumber
        }
    }

}
