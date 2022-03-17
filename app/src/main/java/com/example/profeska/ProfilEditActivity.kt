package com.example.profeska

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import com.example.profeska.databinding.ActivityProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfilEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilEditBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_edit)

       fullScreen(window)

        binding = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var plec ="inne"
        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")

        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("users")
        val id=user.uid

        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
                val rb = findViewById<RadioButton>(checkedId)
                if(rb!=null) {
                    plec = rb.text.toString()
                }
        }

        binding.btnProf.setOnClickListener{
            val name = binding.etUserName.text.toString()
            val sName = binding.etUserSName.text.toString()
            val number = binding.etUserPhon.text.toString()
            val city = binding.etUserCity.text.toString()
            val firebaseInput = DatabaseRow(name,sName,number,city,plec)
            myRef.child(id.toString()).setValue(firebaseInput)
        }

    }

}