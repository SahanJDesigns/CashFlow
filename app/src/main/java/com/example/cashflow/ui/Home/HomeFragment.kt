package com.example.cashflow.ui.Home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.BillBuilder
import com.example.cashflow.BillStages.RequestingBillViewActivity
import com.example.cashflow.SelectClientActivity
import com.example.cashflow.databinding.FragmentHomeBinding
import com.example.cashflow.login.LoginPhoneNumberActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.billList
        val Adapter = BillListRecyclerAdapter(requireActivity(),this)

        recyclerView.adapter = Adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        binding.signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginPhoneNumberActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.creatABill.setOnClickListener {
            val intent = Intent(requireContext(), SelectClientActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}