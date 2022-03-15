package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.profeska.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
//ugabuga nie umiem w gita
    private lateinit var user: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnRegisterTv.setOnClickListener {

            signUpUser()

        }


    }

    private fun signUpUser() {

        user = FirebaseAuth.getInstance()
        if (binding.reEmail.text.toString().isEmpty()) {
            Toast.makeText(this, "Email nie może być pusty!", Toast.LENGTH_SHORT).show()
            return

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.reEmail.toString()).matches()) {
            binding.reEmail.error = "Wprowadź poprawny adres email"
            binding.reEmail.requestFocus()
            return

        }


        if (binding.rePassword.text.toString().isEmpty()) {
            binding.rePassword.error = "Wprowadz poprawne hasło"
            binding.rePassword.requestFocus()
            return

        }


        user.createUserWithEmailAndPassword(
            binding.reEmail.text.toString(),
            binding.rePassword.text.toString()
        )
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Pomyślnie zarejestrowano XDDD", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LogoutActivity::class.java))
                    finish()


                }


            }
    }
}