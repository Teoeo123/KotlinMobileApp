package com.example.profeska

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.example.profeska.databinding.ActivityLogoutBinding
import com.example.profeska.databinding.ActivityMainBinding
import com.example.profeska.databinding.ActivityProfilEditBinding
import com.example.profeska.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfilEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilEditBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_edit)

        binding = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var plec:String = "inne"
        val firebase = FirebaseDatabase.getInstance()

        user = FirebaseAuth.getInstance()
        myRef = Firebase.database.reference
        val id=user.uid

        binding.rgSex.setOnCheckedChangeListener { radioGroup, checkedId ->
                val Rb = findViewById<RadioButton>(checkedId)
                if(Rb!=null) {
                    plec = binding.rNo.text.toString()
                }
        }

        binding.btnProf.setOnClickListener{
            val name = binding.etUserName.text.toString()
            val sName = binding.etUserSName.text.toString()
            val number = binding.etUserPhon.text.toString()
            val city = binding.etUserCity.text.toString()
            val firebaseInput = DatabaseRow(name,sName,number,city,plec)
            myRef.child("users").child(id.toString()).setValue(firebaseInput)
        }

    }

}