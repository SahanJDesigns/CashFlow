package com.example.cashflow.BillStages

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cashflow.Bill
import com.example.cashflow.R
import kotlinx.coroutines.launch

class ProcessingStage:BillState {
    override val id: Int = 1
    override val name: String = "invalid"
    override val billStageCardColor: Int = 83402
    override fun getValidatedStateTransferId(newState: BillState): Int {
        if (newState.id == 2){
            return 2
        }else if(newState.id == 3){
            return 3
        }else{
            return this.id
        }
    }

    override fun getBillView(): Class<out AppCompatActivity> {
        return ProcessingBillViewActivity::class.java
    }
}

class ProcessingBillViewActivity : AppCompatActivity() {
    private  lateinit var bill: Bill
    private lateinit var paidBtn: Button
    private lateinit var urgentBtn: Button
    private lateinit var billIdValue: TextView
    private lateinit var amountValue: TextView
    private lateinit var payeeIdValue: TextView
    private lateinit var payerIdValue: TextView
    private lateinit var remarks: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_processing_bill_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bill = Bill(intent.getStringExtra("bill")!!)

        paidBtn = findViewById(R.id.confirm_paid_btn)
        urgentBtn = findViewById(R.id.convert_to_urgent_btn)
        billIdValue = findViewById(R.id.bill_view_bill_id_value)
        amountValue = findViewById(R.id.bill_view_bill_amount_value)
        payeeIdValue = findViewById(R.id.bill_view_bill_payeeId_value)
        payerIdValue = findViewById(R.id.bill_view_bill_payerId_value)
        remarks = findViewById(R.id.bill_view_bill_remarks_value)


        billIdValue.text = bill.getBillId().toString()
        bill.getBillAmount {
            amountValue.text = it.toString()
        }
        bill.getBillPayeePhoneNum {
            payeeIdValue.text = it
        }
        bill.getBillPayerPhoneNum {
            payerIdValue.text = it
        }
        bill.getBillRemarks {
            remarks.text = it
        }

        paidBtn.setOnClickListener {
            bill.updateState(PaidStage())
        }

        urgentBtn.setOnClickListener {
            bill.updateState(UrgentProcessingStage())
        }
    }
}
