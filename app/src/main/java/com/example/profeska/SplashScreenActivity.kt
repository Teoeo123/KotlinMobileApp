package com.example.profeska

import android.content.Intent
import android.os.Bundle
import android.os.Handler


import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.profeska.databinding.ActivitySplashScreenBinding


class SplashScreenActivity:AppCompatActivity() {

    lateinit var  handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)





        val topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_anmiation)
        val middleAnimation = AnimationUtils.loadAnimation(this,R.anim.middle_animation)
        val top = findViewById<TextView>(R.id.TopTextView)
        val mid= findViewById<TextView>(R.id.MiddleTextView)

       top.startAnimation(topAnimation)
       mid.startAnimation(middleAnimation)





        val splashScreenTimeout = 2500
        val homeIntent = Intent(this, MainActivity::class.java)

        handler = Handler()

       handler.postDelayed({

            startActivity(homeIntent)
            finish()
        },splashScreenTimeout.toLong())


    }

}