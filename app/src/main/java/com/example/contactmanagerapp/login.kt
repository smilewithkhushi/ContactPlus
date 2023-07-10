package com.example.contactmanagerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var firebaseAuth: FirebaseAuth
        lateinit var inputEmail : EditText
        lateinit var inputPass : EditText

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById(R.id.loginEmail)
        inputPass = findViewById(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val signupBtn = findViewById<Button>(R.id.signUpButton)

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
            val email = inputEmail.text.toString()
            val password = inputPass.text.toString()

            if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
        }
            else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful, start the dashboard activity
                            val intent = Intent(this, dashboard::class.java)
                            startActivity(intent)

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
            }

        }

        signupBtn.setOnClickListener {
            val intent=Intent(this, signup::class.java)
            startActivity(intent)
        }
    }
}