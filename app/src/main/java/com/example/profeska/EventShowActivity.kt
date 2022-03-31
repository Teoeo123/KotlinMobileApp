package com.example.profeska

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.profeska.databinding.ActivityEventShowBinding
import com.example.profeska.databinding.ActivityProfilBinding

class EventShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var value :String? = "null"
        val extras = intent.extras
        if (extras != null) {
             value = extras.getString("id")
        }
        else
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            return
        }

        binding.tvId.text=value
    }
}