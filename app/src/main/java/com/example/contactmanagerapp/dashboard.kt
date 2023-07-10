package com.example.contactmanagerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactAdapter(private val contactList: List<addContacts.ContactData>) :
    RecyclerView.Adapter<ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)

    fun bind(contact: addContacts.ContactData) {
        nameTextView.text = contact.name
        numberTextView.text = contact.number
    }
}

class dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var contactAdapter: ContactAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        val userId = auth.currentUser?.uid ?: ""

        val userGreeting = findViewById<TextView>(R.id.username)
        val contactCount= findViewById<TextView>(R.id.contactCount)

        database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val userName = user?.name ?: "User"
                    userGreeting.text = "Welcome $userName"


                    database.child("users").child(userId).child("contacts")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val count = snapshot.childrenCount
                                // Use the count value as needed
                                contactCount.text = "$count Contacts"
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle the error case
                                contactCount.text= " your contacts"
                            }
                        })

                }

                override fun onCancelled(error: DatabaseError) {
                    userGreeting.text = "Hello!"
                }
            })


        val addContactBtn=findViewById<ImageButton>(R.id.addContact)
        addContactBtn.setOnClickListener{
            intent = Intent(this, addContacts::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().reference

        val contactsRecyclerView = findViewById<RecyclerView>(R.id.contactsRecyclerView)
        contactsRecyclerView.layoutManager = LinearLayoutManager(this)

        val contactList = mutableListOf<addContacts.ContactData>()
        contactAdapter = ContactAdapter(contactList)
        contactsRecyclerView.adapter = contactAdapter

        database.child("users").child(userId).child("contacts")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    contactList.clear()
                    for (contactSnapshot in snapshot.children) {
                        val contact = contactSnapshot.getValue(addContacts.ContactData::class.java)
                        if (contact != null) {
                            contactList.add(contact)
                        }
                    }
                    contactAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error case
                }
            })



    }
    data class User(
        val name: String = ""
    )



}