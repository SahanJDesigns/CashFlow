package com.example.cashflow.ui.Profile

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.Bill
import com.example.cashflow.R
import com.example.cashflow.User
import com.google.firebase.auth.FirebaseAuth

class ToReceiveBillRecyclerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<ToReceiveBillRecyclerAdapter.ViewHolder>() {

    private var bills: List<Bill> = emptyList()
    private val user: User = User(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)

    init {
        user.getBillsAtStage(1) { fetchedBills ->
            bills = fetchedBills ?: emptyList()
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.amount)
        val billCard: ConstraintLayout = view.findViewById(R.id.bill_card)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.bill_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bill = bills[position]

        bill.getBillAmount { amount ->
            viewHolder.amount.text = amount?.toString() ?: "N/A"
        }

        viewHolder.billCard.setOnClickListener {
            bill.getBillState { state ->
                state?.let {
                    val intent = Intent(activity, it.getBillView())
                    intent.putExtra("bill", bill.getBillId())
                    activity.startActivity(intent)
                }
            }
        }
    }
    override fun getItemCount() = bills.size
}