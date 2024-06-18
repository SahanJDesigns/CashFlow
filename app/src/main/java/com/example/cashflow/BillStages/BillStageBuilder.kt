package com.example.cashflow.BillStages

class BillStageBuilder {
    companion object {
        fun getBillStateById(id: Int): BillState {
            if (id == 0) {
                return RequestingStage()
            } else if (id == 1) {
                return ProcessingStage()
            } else if (id == 2) {
                return PaidStage()
            } else if (id == 3) {
                return UrgentProcessingStage()
            } else if (id == 4) {
                return RefusedStage()
            } else if (id == 5) {
                return RequestingStage()
            } else {
                return InvalidBillStage(id)
            }
        }
    }
}