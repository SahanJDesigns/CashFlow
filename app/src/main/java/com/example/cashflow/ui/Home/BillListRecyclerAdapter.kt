package com.example.cashflow.ui.Home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cashflow.Bill
import com.example.cashflow.R
import com.example.cashflow.User
import com.google.firebase.auth.FirebaseAuth

class BillListRecyclerAdapter(private val activity: Activity, private val fragment: Fragment) : RecyclerView.Adapter<BillListRecyclerAdapter.ViewHolder>() {

    private var bills: List<Bill> = emptyList()
    private val user: User = User(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)

    init {
        user.getBills { fetchedBills ->
            bills = fetchedBills ?: emptyList()
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profilePic: ImageView = view.findViewById(R.id.client_profile)
        val amount: TextView = view.findViewById(R.id.amount)
        val billStage: TextView = view.findViewById(R.id.bill_stage)
        val billStageCard: CardView = view.findViewById(R.id.bill_stage_card)
        val clientName: TextView = view.findViewById(R.id.client_name)
        val detailBtn: Button = view.findViewById(R.id.bill_details)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.bill_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val bill = bills[position]

        bill.getBillPayeePhoneNum { phoneNum ->
            val client = User(phoneNum)
            client.getUsername { username ->
                viewHolder.clientName.text = username ?: "Unknown"
            }
            updateProfilePic(client, viewHolder.profilePic)
        }

        bill.getBillAmount { amount ->
            viewHolder.amount.text = amount?.toString() ?: "N/A"
        }

        bill.getBillState { state ->
            viewHolder.billStage.text = state?.name ?: "N/A"
            state?.let {
                viewHolder.billStageCard.setBackgroundColor(it.billStageCardColor)
            }
        }

        viewHolder.detailBtn.setOnClickListener {
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

    private fun updateProfilePic(user: User, imageView: ImageView) {
        user.getProfileImageUrl { url ->
            if (url != null) {
                Glide.with(fragment).load(url).into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_profile)
            }
        }
    }
}
