package com.example.project_x.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.project_x.FragmentReplacer
import com.example.project_x.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.MultiFactorSession
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorAssertion
import com.google.firebase.auth.PhoneMultiFactorGenerator
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SendOTP : Fragment() {
    private lateinit var credential: PhoneAuthCredential
    private lateinit var verificationId: String
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    val auth = FirebaseAuth.getInstance()







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
        return inflater.inflate(R.layout.fragment_send_o_t_p, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val user:FirebaseUser = arguments?.getParcelable("user")!!
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1) Instant verification. In some cases, the phone number can be
                //    instantly verified without needing to send or enter a verification
                //    code. You can disable this feature by calling
                //    PhoneAuthOptions.builder#requireSmsValidation(true) when building
                //    the options to pass to PhoneAuthProvider#verifyPhoneNumber().
                // 2) Auto-retrieval. On some devices, Google Play services can
                //    automatically detect the incoming verification SMS and perform
                //    verification without user action.
                this@SendOTP.credential = credential
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in response to invalid requests for
                // verification, like an incorrect phone number.
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Log.w("invalid req",e)// Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    Log.w("too many requests",e)
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number.
                // We now need to ask the user to enter the code and then construct a
                // credential by combining the code with a verification ID.
                // Save the verification ID and resending token for later use.
                this@SendOTP.verificationId = verificationId
                this@SendOTP.forceResendingToken = forceResendingToken
                // ...
            }
        }
        if (user != null) {
            user.multiFactor.session.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val multiFactorSession: MultiFactorSession = task.result
        var sendOTP:Button = view.findViewById(R.id.sendotpbtn)
        sendOTP.setOnClickListener{

                        var phoneNumber1:EditText = view.findViewById(R.id.phone)
                        var phoneNumber = phoneNumber1.text.trim()
                        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
                            .setPhoneNumber(phoneNumber.toString())
                            .setTimeout(30L, TimeUnit.SECONDS)
                            .setMultiFactorSession(multiFactorSession)
                            .setCallbacks(callbacks)
                            .requireSmsValidation(true)
                            .setActivity(requireActivity())
                            .build()


                        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

                    }
                }




        }
    }
        var enterOtp:EditText = view.findViewById(R.id.enterOTPtb)
        var verifyOtp:Button = view.findViewById(R.id.verifyotpbtn)
        verifyOtp.setOnClickListener {
            val verificationCode = enterOtp.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
            val multiFactorAssertion= PhoneMultiFactorGenerator.getAssertion(credential)
            // Complete enrollment. This will update the underlying tokens
            // and trigger ID token change listener.
            FirebaseAuth.getInstance()
                .currentUser
                ?.multiFactor
                ?.enroll(multiFactorAssertion, "My personal phone number")
                ?.addOnCompleteListener {
                    // ...
                    (requireActivity() as FragmentReplacer).setFragment(LoginFragment())
                }

        }



    }
}