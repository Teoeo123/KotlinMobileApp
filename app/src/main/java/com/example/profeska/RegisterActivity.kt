package com.example.profeska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
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
        fullScreen(window)



        binding.btnRegisterTv.setOnClickListener {
            signUpUser()
        }


    }

    private fun signUpUser() {



         val email = binding.reEmail.text.toString()
         val password = binding.rePassword.text.toString()


        user = FirebaseAuth.getInstance()
        if (email.isEmpty()) {
            binding.reEmail.error = "Wprowadź poprawny adres email"
            binding.reEmail.requestFocus()
            return

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.reEmail.error = "Wprowadź poprawny adres email"
            binding.reEmail.requestFocus()
            return

        }
        //żart :D
        if (email.contains("wojcik") || email.contains("wójcik")) {
            binding.reEmail.error = "Nie potrzebujemy cię w naszym składzie"
            binding.reEmail.requestFocus()
            Toast.makeText(this, "Nie no to tylko żart B^)", Toast.LENGTH_LONG).show()
            return

        }



        if (password.isEmpty() or (password.length < 8)) {
            binding.rePassword.error = "Wprowadz poprawne hasło"

            return

        }


        user.createUserWithEmailAndPassword(
            email,
            password
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