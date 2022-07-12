package com.example.mindboxregistrationflow.model

import com.example.mindboxregistrationflow.data.AzureRequest
import io.reactivex.Completable

class AzureInit() {
    fun initAzure(params: AzureRequest): Completable {
        return Completable.complete()//stub
    }
}
