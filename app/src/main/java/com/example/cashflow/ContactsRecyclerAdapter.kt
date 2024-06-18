package com.example.cashflow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsRecyclerAdapter(private val clients: List<User>,private val context: Context) : RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder>() {

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.contact_name)
        val contactNumber: TextView = view.findViewById(R.id.contact_number)
        val contactCard: LinearLayout = view.findViewById(R.id.contact_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val client = clients[position]
        client.getUsername {
            holder.contactName.text = it
        }

        holder.contactNumber.text = client.getUserPhoneNum()
        holder.contactCard.setOnClickListener {
            val intent = Intent(context, BillBuilder::class.java)
            intent.putExtra("client",client.getUserPhoneNum())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return clients.size
    }
}
