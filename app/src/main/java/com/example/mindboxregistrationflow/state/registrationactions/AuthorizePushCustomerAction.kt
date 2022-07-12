package com.example.mindboxregistrationflow.state.registrationactions

import android.os.Build
import android.util.Log
import com.example.mindboxregistrationflow.BuildConfig
import com.example.mindboxregistrationflow.model.MindBoxOperation
import com.example.mindboxregistrationflow.data.MindBoxOperationModel
import com.example.mindboxregistrationflow.data.MindBoxOperationType
import com.example.mindboxregistrationflow.data.getPushSubscriberModel
import com.example.mindboxregistrationflow.state.PushRegistrationAction
import com.example.mindboxregistrationflow.state.PushUtil.deviceUuid
import com.example.mindboxregistrationflow.state.customerAuthorized
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthorizePushCustomerAction : PushRegistrationAction(), KoinComponent {
    private val tag = "AuthorizePushCustomer"
    private val mindBoxOperation: MindBoxOperation by inject()
    private var onSuccess: (() -> Unit)? = null

    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        this.onSuccess = onSuccess
        if (param is String) authorizePushCustomer(param)
    }

    private fun authorizePushCustomer(contractId: String) {
        val pushSubscriberData = getPushSubscriberModel(
                MindBoxOperationType.AUTHORIZE_CUSTOMER,
                contractId,
                Build.BRAND,
                Build.MODEL
        )
        val mindBoxOperationModel = MindBoxOperationModel(
                BuildConfig.MINDBOX_ENDPOINT,
                MindBoxOperationType.AUTHORIZE_CUSTOMER.value,
                deviceUuid,
                pushSubscriberData
        )
        disposable = mindBoxOperation.sendOperation(mindBoxOperationModel)
            .subscribe(
                {
                    Log.d(tag, "authorizing push customer complete")
                    release()
                    customerAuthorized = true
                    onSuccess?.invoke()
                },
                {
                    Log.e(tag, "authorizing push customer error: $it")
                }
            )
    }
}
