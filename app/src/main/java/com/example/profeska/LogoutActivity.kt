package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.profeska.databinding.ActivityLogoutBinding
import com.google.firebase.auth.FirebaseAuth

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLogoutBinding
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
        user = FirebaseAuth.getInstance()


        if(user.currentUser !=null){
            user.currentUser?.let {

                binding.tvUserEmail.text = it.email
                binding.tvUID.text = it.uid
            }
        }


        binding.btnSignOut.setOnClickListener {
            user.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.btnProfil.setOnClickListener {
            startActivity(Intent(this,ProfilActivity::class.java))
        }

        binding.btnEvent.setOnClickListener {
            startActivity(Intent(this,EventsActivity::class.java))
        }

    }
}