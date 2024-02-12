package com.example.project_x.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
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

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.MultiFactorAssertion
import com.google.firebase.auth.MultiFactorInfo
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.PhoneMultiFactorInfo
import java.util.concurrent.TimeUnit





class LoginFragment : Fragment() {


    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var loginBtn: Button
    private lateinit var googleSignINBtn: Button
    private lateinit var signUpEt: TextView
    private lateinit var forgotPass: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var credential: PhoneAuthCredential
    private var verificationId: String = ""
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private var otp:String = ""
    private fun showInputDialog(){
        val editText = EditText(requireContext())

        val dialog = AlertDialog.Builder(activity)
            .setTitle("Enter Otp")
            .setView(editText)
            .setPositiveButton("Confirm") { _: DialogInterface, _: Int ->
                val userInput = editText.text.trim().toString()
                this@LoginFragment.otp = userInput
                // Do something with the user input
                // For example, you can pass it to another function or display it
                // You may also use it to update your UI or perform any desired action
            }


            .create()

        dialog.show()


    }

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
        loginBtn = view.findViewById(R.id.signInButton)
        googleSignINBtn = view.findViewById(R.id.GoogleSignButton)
        forgotPass = view.findViewById(R.id.forgotPass)
        signUpEt = view.findViewById(R.id.createAccountText)

        auth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                this@LoginFragment.credential = credential
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in response to invalid requests for
                // verification, like an incorrect phone number.
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Log.w("inavlid creds",e)
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    Log.w("too many requests",e)
                }

            }

            override fun onCodeSent(
                verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {

                this@LoginFragment.verificationId = verificationId
                this@LoginFragment.forceResendingToken = forceResendingToken
                // ...
            }
        }

        loginBtn.setOnClickListener{

            val email:String = emailEt.text.toString()
            val pass:String = passwordEt.text.toString()

            if(email.isEmpty() || !isValidEmail(email)){
                emailEt.error = "enter valid email address"
                return@setOnClickListener
            }
            if(pass.isEmpty() || pass.length < 6){
                passwordEt.error = "enter correct pass"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener{ task ->


                    if(task.isSuccessful){
                        val user: FirebaseUser? = auth.currentUser

                        if(!user!!.isEmailVerified){
                            Toast.makeText(activity?.applicationContext, "Please verify your email.", Toast.LENGTH_SHORT).show()
                        }
                        val toMainActivity = Intent (activity?.applicationContext, MainActivity::class.java)
                        startActivity(toMainActivity)
                        activity?.finish()

                    }
                    /* if (task.exception is FirebaseAuthMultiFactorException) {

                        val multiFactorResolver = (task.exception as FirebaseAuthMultiFactorException).resolver
                        Log.w("index size", multiFactorResolver.hints.size.toString())
                        val selectedIndex = 1
                        if (multiFactorResolver.hints.isNotEmpty() && selectedIndex <= multiFactorResolver.hints.size) {

                            val selectedHint: PhoneMultiFactorInfo =
                                multiFactorResolver.hints[selectedIndex] as PhoneMultiFactorInfo

                            try {
                                PhoneAuthProvider.verifyPhoneNumber(
                                    PhoneAuthOptions.newBuilder()
                                        .setActivity(requireActivity())
                                        .setMultiFactorSession(multiFactorResolver.session)
                                        .setMultiFactorHint(selectedHint)
                                        .requireSmsValidation(true)
                                        .setCallbacks(callbacks)
                                        .setTimeout(60L, TimeUnit.SECONDS)
                                        .build()
                                )

                                if (verificationId == null && savedInstanceState != null) {
                                    onViewStateRestored(savedInstanceState);
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    activity?.applicationContext,
                                    "$e",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                            try {
                                val editText = EditText(activity)

                                val dialog = AlertDialog.Builder(activity)
                                    .setTitle("Enter Otp")
                                    .setView(editText)
                                    .setPositiveButton("Confirm") { _: DialogInterface, _: Int ->
                                        val userInput = editText.text.trim().toString()
                                        otp = userInput
                                        // Do something with the user input
                                        // For example, you can pass it to another function or display it
                                        // You may also use it to update your UI or perform any desired action
                                    }


                                    .create()

                                dialog.show()

                            } catch (e:Exception){
                                Toast.makeText(activity?.applicationContext, "$e",Toast.LENGTH_LONG).show()
                            }



                            val verificationCode: String = otp

                            Log.w("s", "$verificationId, $verificationCode")
                            val credential =
                                PhoneAuthProvider.getCredential(verificationId, verificationCode)
                            val multiFactorAssertion: MultiFactorAssertion =
                                PhoneMultiFactorGenerator.getAssertion(credential)
                            multiFactorResolver
                                .resolveSignIn(multiFactorAssertion)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            activity?.applicationContext,
                                            "Sign IN successfull",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // User successfully signed in with the
                                        // second factor phone number.
                                        val toMainActivity = Intent(
                                            activity?.applicationContext,
                                            MainActivity::class.java
                                        )
                                        startActivity(toMainActivity)
                                        activity?.finish()
                                    } else {
                                        Log.w(
                                            ContentValues.TAG,
                                            "createUserWithEmail:failure",
                                            task.exception
                                        )
                                        Toast.makeText(
                                            activity?.applicationContext,
                                            "Login Failed.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@addOnCompleteListener
                                    }
                                    // ...
                                }

                    }*/
                    else{
                        Log.w("error", task.exception)
                        val r:java.lang.Exception? = task.exception
                        val toMainActivity = Intent (activity?.applicationContext, MainActivity::class.java)
                        startActivity(toMainActivity)
                        activity?.finish()


                    }


                }





                }
        signUpEt.setOnClickListener {

            (requireActivity() as FragmentReplacer).setFragment(CreateAccountFragment())

        }

        }



}




