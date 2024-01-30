package com.example.project_x.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.project_x.MainActivity
import com.example.project_x.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.project_x.FragmentReplacer


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LoginFragment : Fragment() {


    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var LoginBtn: Button
    private lateinit var googleSignINBtn: Button
    private lateinit var signUpEt: TextView
    private lateinit var forgotPass: TextView
    private lateinit var auth: FirebaseAuth
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEt = view.findViewById(R.id.email1Et)
        passwordEt = view.findViewById(R.id.passwordEt)
        LoginBtn = view.findViewById(R.id.signInButton)
        googleSignINBtn = view.findViewById(R.id.GoogleSignButton)
        forgotPass = view.findViewById(R.id.forgotPass)
        signUpEt = view.findViewById(R.id.createAccountText)

        auth = FirebaseAuth.getInstance()


        LoginBtn.setOnClickListener{

            var email:String = emailEt.text.toString()
            var pass:String = passwordEt.text.toString()

            if(email.isEmpty() || !isValidEmail(email)){
                emailEt.error = "enter valid email address"
                return@setOnClickListener
            }
            if(pass.isEmpty() || pass.length < 6){
                passwordEt.error = "enter correct pass"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(){ task ->


                    if(task.isSuccessful){
                        var user: FirebaseUser? = auth.currentUser

                        if(!user!!.isEmailVerified){
                            Toast.makeText(activity?.applicationContext, "Please verify your email.", Toast.LENGTH_SHORT).show()
                        }
                        val toMainActivity = Intent (activity?.applicationContext, MainActivity::class.java)
                        startActivity(toMainActivity)
                        activity?.finish()



                    } else {
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity?.applicationContext, "Login Failed.", Toast.LENGTH_SHORT).show()
                    }



                }

        }
        signUpEt.setOnClickListener {

            (requireActivity() as FragmentReplacer).setFragment(CreateAccountFragment())

        }
    }


}