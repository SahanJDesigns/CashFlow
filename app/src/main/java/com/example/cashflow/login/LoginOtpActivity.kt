package com.example.cashflow.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflow.MainActivity
import com.example.cashflow.databinding.ActivityLoginOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class LoginOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginOtpBinding
    private lateinit var otpInput: EditText
    private lateinit var nextBtn: Button
    private lateinit var resendOptText: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var phoneNum: String
    private var forceResendingToken: ForceResendingToken? = null
    private lateinit var verificationCode: String
    private val auth = FirebaseAuth.getInstance()
    private var timeOutSeconds = 60L
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNum = intent.getStringExtra("phoneNum")!!
        otpInput = binding.loginOtp
        nextBtn = binding.loginNextBtn
        resendOptText = binding.resendOtpTextview
        progressBar = binding.loginProgressBar

        updateUIProgressMode(false)
        sendOtp(false)

        nextBtn.setOnClickListener {
            val otp = otpInput.text.toString()
            if (otp.isNotEmpty()) {
                signIn(PhoneAuthProvider.getCredential(verificationCode, otp))
            } else {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }

        resendOptText.setOnClickListener {
            sendOtp(true)
        }
    }

    private fun sendOtp(isResend: Boolean) {
        startResendTimer()
        updateUIProgressMode(true)

        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNum)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signIn(phoneAuthCredential)
                    updateUIProgressMode(false)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@LoginOtpActivity, "Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    updateUIProgressMode(false)
                }

                override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                    super.onCodeSent(verificationId, token)
                    verificationCode = verificationId
                    forceResendingToken = token
                    updateUIProgressMode(false)
                }
            })

        if (isResend && forceResendingToken != null) {
            optionsBuilder.setForceResendingToken(forceResendingToken!!)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    private fun updateUIProgressMode(isProcessing: Boolean) {
        progressBar.visibility = if (isProcessing) View.VISIBLE else View.GONE
        nextBtn.visibility = if (isProcessing) View.GONE else View.VISIBLE
    }

    private fun signIn(phoneAuthCredential: PhoneAuthCredential) {
        updateUIProgressMode(true)
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener {
            updateUIProgressMode(false)
            if (it.isSuccessful) {
                val intent = Intent(this, LoginUsernameActivity::class.java)
                intent.putExtra("phoneNum", phoneNum)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "OTP verification failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startResendTimer() {
        resendOptText.isEnabled = false
        val startedTimeOut = 60L
        timeOutSeconds = startedTimeOut
        val handler = Handler(Looper.getMainLooper())

        timer?.cancel()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    resendOptText.text = "Resend OTP in $timeOutSeconds seconds"
                    if (timeOutSeconds <= 0) {
                        resendOptText.isEnabled = true
                        resendOptText.text = "Resend OTP"
                        timer?.cancel()
                    }
                }
                timeOutSeconds--
            }
        }, 0, 1000)
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
