package com.example.profeska

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import com.example.profeska.databinding.ActivityProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfilEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilEditBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth
    var plec ="xmr"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profil_edit)




        binding = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)


        user = FirebaseAuth.getInstance()
        val id=user.uid

         fun readUData(user:String?, ){

            binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
                val rb = findViewById<RadioButton>(checkedId)
                if(rb!=null) {
                    plec = rb.text.toString()
                }
            }



            val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            myRef = firebase.getReference("users").child(user.toString())
            myRef.get().addOnSuccessListener {

                if(it.exists()){
                    binding.etUserName.hint="Imie: ${it.child("name").value.toString()}"
                    binding.etUserSName.hint="Nazwisko: ${it.child("sname").value.toString()}"
                    binding.etUserCity.hint="Miasto: ${it.child("city").value.toString()}"
                    binding.etUserPhon.hint="Numer: ${it.child("number").value.toString()}"
                    //binding.rgSex="Płeć: ${it.child("sex").value.toString()}"
                }

            }
        }



        readUData(id)


        binding.btnAcc.setOnClickListener {




            editProfile()


        }
        binding.btnCancel.setOnClickListener(){
            Toast.makeText(this, "Odrzucono zmiany", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ProfilActivity::class.java))


        }



}

    fun editProfile(){

        val firebase = FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("users")
        val id=user.uid




        val name = binding.etUserName.text.toString()
        val sName = binding.etUserSName.text.toString()
        val number = binding.etUserPhon.text.toString()
        val city = binding.etUserCity.text.toString()

        if(plec=="xmr"){
            Toast.makeText(this, "Uzupełnij płeć", Toast.LENGTH_SHORT).show()
            return
        }


        if (name.isEmpty()) {

            Toast.makeText(this, "Uzupełnij imię!", Toast.LENGTH_SHORT).show()
            return
        }
        if (sName.isEmpty()) {

            Toast.makeText(this, "Uzupełnij nazwisko!", Toast.LENGTH_SHORT).show()
            return
        }

        if (number.isEmpty() && number.length < 9) {

            Toast.makeText(this, "Niepoprawny numer telefonu!", Toast.LENGTH_SHORT).show()
            return
        }

        if (city.isEmpty()) {

            Toast.makeText(this, "Wpisz miasto!", Toast.LENGTH_SHORT).show()
            return
        }

        val firebaseInput = DatabaseRow(name, sName, number, city, plec)




            myRef.child(id.toString()).setValue(firebaseInput)
            startActivity(Intent(this, ProfilActivity::class.java))


    }
}















