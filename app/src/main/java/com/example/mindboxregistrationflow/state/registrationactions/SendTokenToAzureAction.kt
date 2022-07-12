package com.example.mindboxregistrationflow.state.registrationactions

import android.util.Log
import com.example.mindboxregistrationflow.data.AzureInitializationData
import com.example.mindboxregistrationflow.data.AzureRequest
import com.example.mindboxregistrationflow.model.AzureInit
import com.example.mindboxregistrationflow.state.PushRegistrationAction
import com.example.mindboxregistrationflow.state.PushUtil.hubName
import com.example.mindboxregistrationflow.state.PushUtil.installationId
import com.example.mindboxregistrationflow.state.azureRegistrationComplete
import org.koin.core.KoinComponent
import org.koin.core.inject

class SendTokenToAzureAction : PushRegistrationAction(), KoinComponent {
    private val tag = "SendTokenToAzure"
    private val azureInit: AzureInit by inject()
    private var onSuccess: (() -> Unit)? = null

    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        this.onSuccess = onSuccess
        if (param is String) sendTokenToAzure(param)
    }

    private fun sendTokenToAzure(token: String) {
        val initData = AzureInitializationData(installationId = installationId, pushChannel = token)
        val azureRequest = AzureRequest(hubName, installationId, initData)
        Log.d(tag, "send FB token to  Azure: $token AzureInitializationData: $initData")
        disposable = azureInit.initAzure(azureRequest)
            .subscribe(
                {
                    Log.d(tag, "sending FB token complete")
                    release()
                    azureRegistrationComplete = true
                    onSuccess?.invoke()
                },
                {
                    Log.e(tag, "sending FB token error: $it")
                }
            )
    }
}
