package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginpage.databinding.ActivityAloginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AloginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAloginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAloginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")


        binding.loginSupport.setOnClickListener {
            showSupportDialog()
        }


        binding.loginCust.setOnClickListener {
            val intent = Intent(this@AloginActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.loginButton.setOnClickListener {
            val adminId = binding.loginUsername.text.toString()
            val adminPass = binding.loginPassword.text.toString()

            // Simple security check
            if (adminId == "admin123" && adminPass == "root@admin") {
                Toast.makeText(this, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showSupportDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Contact Support")
        builder.setMessage("Helpline Numbers:\n\n1. +91 8800402287\n2. +91 8800402287")
        builder.setPositiveButton("OK"){ dialog, which ->
            dialog.dismiss()
        }

        builder.setNeutralButton("Call Now") { _, _ ->
            val phoneNumber = "+918800402287"
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = android.net.Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.show()
    }
}
