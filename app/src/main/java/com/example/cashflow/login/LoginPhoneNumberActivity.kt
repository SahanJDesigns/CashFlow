package com.example.cashflow.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflow.databinding.ActivityLoginOtpBinding
import com.example.cashflow.databinding.ActivityLoginPhoneNumberBinding
import com.hbb20.CountryCodePicker

class LoginPhoneNumberActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLoginPhoneNumberBinding
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var mobileNumber:EditText
    private lateinit var sendBtn:Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countryCodePicker = binding.countryCodePicker
        mobileNumber = binding.loginMobileNumber
        sendBtn = binding.sendOtpBtn
        progressBar = binding.loginProgressBar


        sendBtn.setOnClickListener{
            countryCodePicker.registerCarrierNumberEditText(mobileNumber)
            if(countryCodePicker.isValidFullNumber){
                val intent = Intent(this,LoginOtpActivity::class.java)
                intent.putExtra("phoneNum",countryCodePicker.fullNumberWithPlus)
                startActivity(intent)
            }else{
                mobileNumber.setError("Phone number not valid")
            }
        }
    }
}