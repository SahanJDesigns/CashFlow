package com.example.cashflow

import com.example.cashflow.BillStages.BillStageBuilder
import com.example.cashflow.BillStages.BillState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.io.Serializable

class Bill:Serializable{
    private val firestore = Firebase.firestore
    private var id:String
    constructor(amount:Double,payeePhoneNum:String,payerPhoneNum:String,billState:BillState,remarks:String){
        val documentRef = firestore.collection("bills").document()
        id = documentRef.id
        val data = hashMapOf(
            "id" to id,
            "amount" to amount,
            "payeePhoneNum" to payeePhoneNum,
            "payerPhoneNum" to payerPhoneNum,
            "billState" to billState.id,
            "remarks" to remarks
        )
        documentRef.set(data)
    }
    constructor(id:String){
        this.id =id
    }
    fun getBillId():String{
        return id
    }
    fun getBillPayeePhoneNum(callback: (String) -> Unit){
        val userDocRef = firestore.collection("bills").document(id)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val payeePhoneNum = document.get("payeePhoneNum") as? String ?: ""
                    callback(payeePhoneNum)
                } else {
                    callback("")
                }
            }
    }

    fun getBillPayerPhoneNum(callback: (String) -> Unit){
        val userDocRef = firestore.collection("bills").document(id)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val payerPhoneNum = document.get("payerPhoneNum") as? String ?: ""
                    callback(payerPhoneNum)
                } else {
                    callback("")
                }
            }
    }
    fun getBillAmount(callback: (Double) -> Unit){
        val userDocRef = firestore.collection("bills").document(id)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val amount = document.getDouble("amount") ?:0.0
                    callback(amount)
                } else {
                    callback(0.0)
                }
            }
    }

    fun getBillRemarks(callback: (String) -> Unit){
        val userDocRef = firestore.collection("bills").document(id)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val Remarks = document.getString("remarks") ?:""
                    callback(Remarks)
                } else {
                    callback("")
                }
            }
    }
    fun getBillState(callback: (BillState) -> Unit){
        val userDocRef = firestore.collection("bills").document(id)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val stateId = document.getLong("billState") ?:-1
                    val state = BillStageBuilder.getBillStateById(stateId.toInt())
                    callback(state)
                } else {
                    callback(BillStageBuilder.getBillStateById(-1))
                }
            }
    }
    fun updateState(newState:BillState){
            val billState = getBillState {
                val userDocRef = firestore.collection("bills").document(id).update("billState",it.getValidatedStateTransferId(newState))
            }
        }
    }
