package com.example.mindboxregistrationflow.model

import com.example.mindboxregistrationflow.data.MindBoxInstallationData
import io.reactivex.Single

class MindBoxInit() {
    fun initMindBox(params: String): Single<MindBoxInstallationData> {
        return Single.error(Exception())//stub
    }
}
