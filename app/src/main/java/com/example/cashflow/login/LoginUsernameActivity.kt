package com.example.cashflow.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflow.MainActivity
import com.example.cashflow.User
import com.example.cashflow.databinding.ActivityLoginUsernameBinding

class LoginUsernameActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLoginUsernameBinding
    private lateinit var phoneNum:String
    private lateinit var usernameField: EditText
    private lateinit var letMeInBtn:Button
    private  lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneNum = intent.getStringExtra("phoneNum")!!
        usernameField =binding.loginUsername
        letMeInBtn = binding.loginLetMeInBtn
        progressBar = binding.loginProgressBar

        usernameField.setOnClickListener{
            val username = usernameField.text.toString()
            if(!username.isEmpty()){
                val user = User(phoneNum,username)
                val intent = Intent(this,MainActivity::class.java)
                intent.putExtra("phoneNum",phoneNum)
                startActivity(intent)
            }else{
                usernameField.setError("Enter valid username")
            }
        }

    }

}