package com.example.cashflow


import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class  User{
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var phoneNum: String
    constructor(phoneNum: String,username: String){
        this.phoneNum = phoneNum
        val userDocRef = firestore.collection("users").document(phoneNum)
        val data = hashMapOf(
            "PhoneNum" to phoneNum,
            "username" to username,
            "bills" to emptyList<String>()
        )
        userDocRef.set(data)
    }
    constructor(phoneNum: String){
        this.phoneNum = phoneNum
    }

    fun getUserPhoneNum(): String {
        return phoneNum
    }

    fun getUsername(callback: (String?) -> Unit) {
        val userDocRef = firestore.collection("users").document(phoneNum)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username")
                    callback(username)
                } else {
                    callback(null)
                }
            }
    }

    fun setUsername(username: String) {
        firestore.collection("users").document(phoneNum).update("username", username)
    }

    fun getBills(callback: (ArrayList<Bill>?) -> Unit) {
        val userDocRef = firestore.collection("users").document(phoneNum)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val bills = document.get("bills") as? List<String> ?: emptyList()
                    val billsArrayList = ArrayList(bills.map { Bill(it) })
                    callback(billsArrayList)
                } else {
                    callback(null)
                }
            }
    }

    fun getBillsAtStage(stageId: Int, callback: (ArrayList<Bill>?) -> Unit) {
        val userDocRef = firestore.collection("users").document(phoneNum)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val bills = document.get("bills") as? List<String> ?: emptyList()
                    val billsArrayList: ArrayList<Bill> = ArrayList()

                    var count = 0
                    for (billId in bills) {
                        val bill = Bill(billId)
                        bill.getBillState { billState ->
                            if (billState != null && billState.id == stageId) {
                                billsArrayList.add(bill)
                            }
                            count++
                            if (count == bills.size) {
                                callback(billsArrayList)
                            }
                        }
                    }
                } else {
                    callback(null)
                }
            }
    }


    fun addBill(bill: Bill) {
        firestore.collection("users").document(phoneNum)
            .update("bills", FieldValue.arrayUnion(bill.getBillId()))
    }

    fun removeBill(bill: Bill) {
        firestore.collection("users").document(phoneNum)
            .update("bills", FieldValue.arrayRemove(bill.getBillId()))
    }
    fun getProfileImageUrl(callback: (String?) -> Unit) {
        val userDocRef = firestore.collection("users").document(phoneNum)

        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profileImageUrl = document.getString("profileImageUrl")
                    callback(profileImageUrl)
                } else {
                    callback(null)
                }
            }
    }
}
