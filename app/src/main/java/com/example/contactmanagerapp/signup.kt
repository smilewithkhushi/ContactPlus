package com.example.contactmanagerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEditText = findViewById(R.id.signupName)
        emailEditText = findViewById(R.id.signupEmail)
        passwordEditText = findViewById(R.id.signupPassword)

        val firebaseAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        //sign up button code
        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign up successful
                        //val user = firebaseAuth.currentUser

                        // Store the user data in the realtime database
                        val database = FirebaseDatabase.getInstance()
                        val dbRef= database.reference.child("users")
                        val data = User(name, email)

                        val userID=dbRef.push().key

                        val userRef = dbRef.child(userID!!)
                        userRef.setValue(data)

                        Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                    } else {
                        // Sign up failed
                        Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginButton.setOnClickListener {
            intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }

        //show password checkbox
        val checkBoxShowPassword = findViewById<CheckBox>(R.id.checkBox)
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            // Toggle the input type of the EditText based on the checkbox state
            if (isChecked) {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Move the cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.length())
        }
    }

    // Define a data class to represent your data structure
    data class User(val name: String, val mail: String)

}