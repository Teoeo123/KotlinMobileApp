package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.profeska.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseAuth





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)
        user = FirebaseAuth.getInstance()

        checkIfUserIsLogged()

        binding.btnLogin.setOnClickListener{
            signUser()
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))

        }


    }

    private fun checkIfUserIsLogged(){
        if(user.currentUser != null){

            startActivity(Intent(this,LogoutActivity::class.java))


        }
    }

    private fun signUser(){

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
                        user.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener{ mTask->

                                if(mTask.isSuccessful){
                                    startActivity(Intent(this,LogoutActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this,"Błędny email lub hasło",Toast.LENGTH_SHORT).show()
                                }
                            }
        }
    }
}

