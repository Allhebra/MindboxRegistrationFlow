package com.example.mindboxregistrationflow.model

import com.example.mindboxregistrationflow.data.MindBoxOperationModel
import io.reactivex.Completable

class MindBoxOperation {
    fun sendOperation(params: MindBoxOperationModel): Completable {
        return Completable.complete()//stub
    }
}
