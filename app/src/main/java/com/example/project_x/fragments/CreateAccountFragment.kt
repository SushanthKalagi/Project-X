package com.example.project_x.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.project_x.FragmentReplacer
import com.example.project_x.MainActivity
import com.example.project_x.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects


class CreateAccountFragment : Fragment() {


    private lateinit var nameEt:EditText
    private lateinit var emailEt:EditText
    private lateinit var passwordEt:EditText
    private lateinit var confirmPassEt:EditText
    private lateinit var loginTextEt: TextView
    private lateinit var signUpBtnEt: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var enable2fa: CheckBox
    private var progressStatus = 0
    private lateinit var handler: Handler

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEt = view.findViewById(R.id.name)
        emailEt = view.findViewById(R.id.email)
        passwordEt = view.findViewById(R.id.password)
        confirmPassEt = view.findViewById(R.id.confirmPass)
        loginTextEt = view.findViewById(R.id.backToLoginText)
        signUpBtnEt = view.findViewById(R.id.signUpButton)
        enable2fa = view.findViewById(R.id.enable2FAcb)

        auth = FirebaseAuth.getInstance()


        loginTextEt.setOnClickListener{
            (requireActivity() as FragmentReplacer).setFragment(LoginFragment())
        }
        signUpBtnEt.setOnClickListener {
            handler = Handler(Looper.getMainLooper())

            // Start a background thread to simulate progress updates
            Thread {
                while (progressStatus < 100) {
                    progressStatus += 1

                    // Update the progress bar on the UI thread
                    handler.post {
                        var progressBar:ProgressBar = view.findViewById(R.id.progressBar)
                        var textViewProgress:TextView = view.findViewById(R.id.textViewProgress)
                        progressBar.progress = progressStatus
                        textViewProgress.text = "$progressStatus%"
                    }

                    // Simulate a delay to show progress updates
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
            val name = nameEt.text.toString()
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()
            val confirmPass: String = confirmPassEt.text.toString()

            if (name.isEmpty() || name == " "){
                nameEt.error = "Please Enter Valid Name"
                return@setOnClickListener
            }

            if (email.isEmpty() || !isValidEmail(email)){
                emailEt.error = "Please Enter a Valid Email Address"
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6){
                passwordEt.error = "Please Enter a Valid Password"
                return@setOnClickListener
            }

            if (password != confirmPass){
                passwordEt.error = "Passwords dont match"
                return@setOnClickListener
            }

            createAccount(name,email,password)
        }

    }

    private fun createAccount(name: String, email: String, pass: String){

        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(){ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user : FirebaseUser? = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener(){task ->
                            if(task.isSuccessful){
                                Toast.makeText(activity?.applicationContext, "verification link sent to $email", Toast.LENGTH_SHORT).show()
                            }


                        }
                    updateUI(user, name, email)
                } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)

                Toast.makeText(activity?.applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

            }


            }

    }

    private fun updateUI(user: FirebaseUser?, name: String, email: String){

        var hashMap : HashMap<String, String>  = HashMap<String, String> ()
        hashMap["name"] = name
        hashMap["email"] = email
        hashMap["uid"] = user!!.uid
        val bundle = Bundle().apply {
            putParcelable("user", user)
        }

        // Navigate to SendOTP fragment with the bundle
        val sendOTPFragment = SendOTP().apply {
            arguments = bundle
        }

        FirebaseFirestore.getInstance().collection("Users").document(user.uid)
            .set(hashMap)
            .addOnCompleteListener{task ->
                if(task.isSuccessful and enable2fa.isChecked){
                    (requireActivity() as FragmentReplacer).setFragment(sendOTPFragment)


                }else if (task.isSuccessful and !enable2fa.isChecked){
                    (requireActivity() as FragmentReplacer).setFragment(LoginFragment())
                }

                else {
                    Toast.makeText(activity?.applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }

            }

    }
}