package com.example.loginpage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginpage.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.loginButton.setOnClickListener {
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if(loginUsername.isNotEmpty() && loginPassword.isNotEmpty()){
                loginUser(loginUsername, loginPassword)
            }else{
                Toast.makeText(this@LoginActivity,"All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginSupport.setOnClickListener {
            showSupportDialog()
        }

        binding.loginAdmin.setOnClickListener {
            val intent = Intent(this@LoginActivity, AloginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(id: String, password: String){
        databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    for (userSnapshot in dataSnapshot.children){
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData!=null && userData.password == password){
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
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