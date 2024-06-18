package com.example.cashflow

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cashflow.BillStages.RequestingStage
import com.google.firebase.auth.FirebaseAuth

class BillBuilder : AppCompatActivity() {

    private lateinit var amountField: EditText
    private lateinit var payerPhoneNumField: EditText
    private lateinit var remarksField: EditText
    private lateinit var requestBtn: Button
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_bill)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        amountField = findViewById(R.id.amount_input)
        payerPhoneNumField = findViewById(R.id.payer_input)
        remarksField = findViewById(R.id.remarks_input)
        requestBtn = findViewById(R.id.create_confirm_bill_btn)

        val currentUserPhoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: ""
        user = User(currentUserPhoneNumber)

        val clientPhoneNum = intent.getStringExtra("client")
        clientPhoneNum?.let {
            payerPhoneNumField.setText(it)
        }

        requestBtn.setOnClickListener {
            createBill()
        }
    }

    private fun createBill() {
        try {
            val amount = amountField.text.toString().toDouble()
            val payerPhoneNum = payerPhoneNumField.text.toString()
            val remarks = remarksField.text.toString()

            val bill = Bill(
                amount,
                user.getUserPhoneNum(),
                payerPhoneNum,
                RequestingStage(),
                remarks
            )

            val client = User(payerPhoneNum)
            client.addBill(bill)
            user.addBill(bill)

            Toast.makeText(this, "Bill created successfully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to create bill: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
