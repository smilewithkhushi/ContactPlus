package com.example.contactmanagerapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class addContacts : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var nameEditText : EditText
    lateinit var numberEditText : EditText
    lateinit var emailEditText : EditText
    lateinit var birthdayEditText : EditText
    lateinit var notesEditText : EditText

    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contacts)

        database = FirebaseDatabase.getInstance().reference
        nameEditText=findViewById<EditText>(R.id.nameEditText)
        numberEditText=findViewById<EditText>(R.id.numberEditText)
        emailEditText=findViewById<EditText>(R.id.emailEditText)
        birthdayEditText=findViewById<EditText>(R.id.birthdayEditText)
        notesEditText=findViewById<EditText>(R.id.notesEditText)

        val saveButton=findViewById<Button>(R.id.saveContactBtn)
        saveButton.setOnClickListener {
            saveContact()
        }

        val cancelButton=findViewById<Button>(R.id.cancelBtn)
        cancelButton.setOnClickListener {
            finish()
        }

        val uploadContactImage=findViewById<ImageButton>(R.id.uploadContactImg)
//        uploadContactImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            pickImageLauncher.launch(intent)
//        }
    }

    private fun saveContact() {
        val name = nameEditText.text.toString()
        val number = numberEditText.text.toString()
        val email = emailEditText.text.toString()
        val birthday = birthdayEditText.text.toString()

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val contactData = ContactData(name, number, email, birthday)

        //val contactId = database.child("users").child(userId).child("contacts").push().key
        val contactId = number
        if (contactId != null) {

//            val imageRef = database.child("users").child(userId).child("contacts").child("image")
//            imageRef.putFile(selectedImageUri)
            database.child("users").child(userId).child("contacts").child(number).setValue(contactData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show()
                    intent = Intent(this, dashboard::class.java)
                    startActivity(intent)
                    finish()
                // Return to the previous activity after saving the contact
                }
                .addOnFailureListener {
                    Toast.makeText(this, "The contact couldn't be saved! Try again", Toast.LENGTH_SHORT).show()
                    // Handle the failure case
                }
        }
    }

//    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        if (uri != null) {
//            selectedImageUri = uri
//            val imageView = findViewById<ImageButton>(R.id.uploadContactImg)
//            imageView.setImageURI(selectedImageUri)
//        }
//    }

    data class ContactData(
        val name: String = "",
        val number: String = "",
        val email: String = "",
        val birthday: String = "",
        val notes: String=""
    )
}
