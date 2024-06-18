package com.example.cashflow.BillStages

import androidx.appcompat.app.AppCompatActivity

interface BillState {
    val id: Int
    val name:String
    val billStageCardColor:Int
    fun getValidatedStateTransferId(newState:BillState):Int
    fun getBillView(): Class<out AppCompatActivity>
}