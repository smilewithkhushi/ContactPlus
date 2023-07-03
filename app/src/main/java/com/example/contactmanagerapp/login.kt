package com.example.contactmanagerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        lateinit var firebaseAuth: FirebaseAuth


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val inputEmail = findViewById<EditText>(R.id.loginEmail)
        val inputPass = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signupBtn = findViewById<Button>(R.id.signUpButton)

        val email = inputEmail.text.toString().trim()
        val password = inputPass.text.toString().trim()

        //show password button
        val checkBoxShowPassword = findViewById<CheckBox>(R.id.checkBox)
        checkBoxShowPassword.setOnCheckedChangeListener { _, isChecked ->
            // Toggle the input type of the EditText based on the checkbox state
            if (isChecked) {
                inputPass.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                inputPass.inputType = android.text.InputType.TYPE_CLASS_TEXT or
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            // Move the cursor to the end of the text
            inputPass.setSelection(inputPass.length())
        }

        //on click login button
        firebaseAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful, start the dashboard activity
                            val intent = Intent(this, dashboard::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Login failed, display an error message
                            val exception = task.exception
                            when (exception) {
                                is FirebaseAuthInvalidUserException ->
                                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()

                                is FirebaseAuthInvalidCredentialsException ->
                                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT)
                                        .show()

                                else ->
                                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signupBtn.setOnClickListener {
            val intent=Intent(this, signup::class.java)
            startActivity(intent)
        }
    }
}