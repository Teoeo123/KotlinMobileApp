package com.example.profeska

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.UriMatcher
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.profeska.databinding.ActivityProfilEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class ProfilEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilEditBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var user: FirebaseAuth
    private lateinit var imageUri: Uri
    private  var plec = "xmr"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profil_edit)

        binding = ActivityProfilEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fullScreen(window)

        val firebase =
            FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("users")
        val id = user.uid

        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            if (rb != null) {
                plec = rb.text.toString()
            }
        }




        fun readUData(user: String?, ) {


            val imageName = "basic-profil.png"
            val storageRefIfAdd= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$id")
            val storageRef= FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$imageName")
            val localFile = File.createTempFile("temp","png")

            storageRefIfAdd.getFile(localFile).addOnSuccessListener {
                val bitmap =BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("ProfPic","Success")
                binding.profPicEdit.setImageBitmap(bitmap)
            }.addOnFailureListener{
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap =BitmapFactory.decodeFile(localFile.absolutePath)
                    Log.d("ProfPic","Success")
                    binding.profPicEdit.setImageBitmap(bitmap)
                }.addOnFailureListener {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }







            binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
                val rb = findViewById<RadioButton>(checkedId)
                if (rb != null) {
                    plec = rb.text.toString()
                }
            }


            val firebase =
                FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
            myRef = firebase.getReference("users").child(user.toString())
            myRef.get().addOnSuccessListener {

                if (it.exists()) {
                    binding.etUserName.setText(it.child("name").value.toString())
                    binding.etUserSName.setText(it.child("sname").value.toString())
                    binding.etUserCity.setText(it.child("city").value.toString())
                    binding.etUserPhon.setText(it.child("number").value.toString())
                    val tmp = it.child("sex").value.toString()

                    if(tmp=="Mężczyzna"){
                        binding.rMa.isChecked=true
                    }
                    if(tmp=="Kobieta"){
                        binding.rFe.isChecked=true
                    }
                    if(tmp=="Inne"){
                        binding.rNo.isChecked=true
                    }

                }
                else{
                    binding.btnAcc.isEnabled=false
                    binding.btnAcc.alpha=0.3.toFloat()
                    binding.btnCancel.isEnabled=false
                    binding.btnCancel.alpha=0.toFloat()
                }

            }
        }


        readUData(id)

        changeWatcher()


        binding.btnAcc.setOnClickListener {


            editProfile()


        }

        binding.btnCancel.setOnClickListener() {
            Toast.makeText(this, "Odrzucono zmiany", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LogoutActivity::class.java))


        }


        binding.profPicEdit.setOnClickListener {
            Log.d("Zdjecie", "dziala")
            selectImage()
        }

    }

    private fun editProfile() {
        val firebase =
            FirebaseDatabase.getInstance("https://profeska-ad23d-default-rtdb.europe-west1.firebasedatabase.app")
        user = FirebaseAuth.getInstance()
        myRef = firebase.getReference("users")
        val id = user.uid


        val name = binding.etUserName.text.toString()
        val sName = binding.etUserSName.text.toString()
        val number = binding.etUserPhon.text.toString()
        val city = binding.etUserCity.text.toString()




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
        if (plec == "xmr") {
            Toast.makeText(this, "Uzupełnij płeć", Toast.LENGTH_SHORT).show()
            return
        }

        myRef.child(id.toString()).child("city").setValue(city)
        myRef.child(id.toString()).child("name").setValue(name)
        myRef.child(id.toString()).child("sname").setValue(sName)
        myRef.child(id.toString()).child("number").setValue(number)
        myRef.child(id.toString()).child("sex").setValue(plec)

        startActivity(Intent(this, LogoutActivity::class.java))
        if (imageUri != null) {
            uploadImage()
        }

    }


    private fun uploadImage() {
        val fileName = user.uid
        val storageRef =
            FirebaseStorage.getInstance("gs://profeska-ad23d.appspot.com").reference.child("users/$fileName")
        storageRef.putFile((imageUri))
            .addOnSuccessListener {

                binding.profPicEdit.setImageURI(null)
                Log.d("ADDING", "SUCCESS")
            }
            .addOnFailureListener {
                Log.d("ADDING", "REJECTED")
            }

    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!

            binding.profPicEdit.setImageURI(imageUri)
            Log.d("TEST", "$imageUri")
        }
    }

    private fun legitCheck(){
        if(binding.etUserName.text.isNotEmpty()
            && binding.etUserSName.text.isNotEmpty()
            && binding.etUserCity.text.isNotEmpty()
            && binding.etUserPhon.text.isNotEmpty()

        ){
            binding.btnAcc.isEnabled=true
            binding.btnAcc.alpha=1.toFloat()
        }
    }


    private fun changeWatcher(){
        binding.etUserName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.etUserSName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.etUserPhon.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }

        })

        binding.etUserCity.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                legitCheck()
            }


        }


        )

    }


}

