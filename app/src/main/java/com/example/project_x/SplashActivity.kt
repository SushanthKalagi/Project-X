package com.example.project_x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? =auth.currentUser


        Handler(Looper.getMainLooper()).postDelayed({

            if (user == null){
                val mainIntent = Intent(this, FragmentReplacer::class.java)
                startActivity(mainIntent)

            }else{
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }

            finish()
        }, 3000)





    }
}