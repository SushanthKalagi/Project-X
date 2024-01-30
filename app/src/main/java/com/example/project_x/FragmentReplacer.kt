package com.example.project_x

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.project_x.fragments.CreateAccountFragment
import com.example.project_x.fragments.LoginFragment

class FragmentReplacer : AppCompatActivity() {


    private lateinit var frameLayout: FrameLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_replacer)
        frameLayout = findViewById<FrameLayout>(R.id.frame_layout)
        val loginFragment: Fragment = LoginFragment()
        setFragment(loginFragment)



    }
    fun setFragment (fragment: Fragment){
        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        if(fragment is CreateAccountFragment){
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.replace(frameLayout.id,fragment)
        fragmentTransaction.commit()
    }
}