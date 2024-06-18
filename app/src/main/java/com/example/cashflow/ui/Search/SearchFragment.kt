package com.example.cashflow.ui.Search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cashflow.BillStages.*
import com.example.cashflow.R
import com.example.cashflow.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchRecyclerAdapter
    private val stageMap = mapOf(
        "Requesting Stage" to RequestingStage(),
        "Processing Stage" to ProcessingStage(),
        "Paid Stage" to PaidStage(),
        "Urgent Processing Stage" to UrgentProcessingStage(),
        "Refused Stage" to RefusedStage(),
        "Re-requesting Stage" to ReRequestingStage(),
        "Invalid Stage" to null // Handle invalid stage if necessary
    )
    private lateinit var stages:Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        stages = resources.getStringArray(R.array.bill_state)
        return binding.root
    }

    override fun onResume() {
        super.onResume()


        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, stages)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        adapter = SearchRecyclerAdapter(requireActivity())
        binding.confirmBillList.adapter = adapter
        binding.confirmBillList.layoutManager = LinearLayoutManager(context)

        val selection = stageMap[binding.autoCompleteTextView.text.toString()]
        updateRecyclerViewData(selection!!.id)
        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            updateRecyclerViewData(position)
        }
    }

    private fun updateRecyclerViewData(selectedStageId: Int) {

        val selectedStage = stageMap[stages[selectedStageId]]
        if (selectedStage != null) {
            adapter.update(selectedStage)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
