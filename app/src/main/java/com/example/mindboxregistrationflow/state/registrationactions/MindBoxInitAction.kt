package com.example.mindboxregistrationflow.state.registrationactions

import android.util.Log
import com.example.mindboxregistrationflow.BuildConfig
import com.example.mindboxregistrationflow.model.MindBoxInit
import com.example.mindboxregistrationflow.state.*
import com.example.mindboxregistrationflow.state.PushUtil.saveInstallationData
import org.koin.core.KoinComponent
import org.koin.core.inject

class MindBoxInitAction : PushRegistrationAction(), KoinComponent {
    private val tag = "MindBoxInitAction"
    private val mindBoxInit: MindBoxInit by inject()
    private var onSuccess: (() -> Unit)? = null

    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        this.onSuccess = onSuccess
        disposable = mindBoxInit.initMindBox(BuildConfig.MINDBOX_ENDPOINT)
            .subscribe(
                {
                    Log.d(tag, "MindBoxInstallationData loaded: $it")
                    release()
                    saveInstallationData(it)
                    azureRegistrationComplete = false
                    pushSubscriberRegistered = false
                    customerAuthorized = false
                    onSuccess?.invoke()
                },
                {
                    Log.d(tag, "MindBoxInstallationData loading error: $it")
                }
            )
    }
}
