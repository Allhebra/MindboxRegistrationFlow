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
import com.example.mindboxregistrationflow.state.pushSubscriberRegistered
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class SubscribeToMobilePushAction : PushRegistrationAction(), KoinComponent {
    private val tag = "SubscribeToMobilePush"
    private val mindBoxOperation: MindBoxOperation by inject()
    private var onSuccess: (() -> Unit)? = null

    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        this.onSuccess = onSuccess
        subscribeToMobilePush()
    }

    private fun subscribeToMobilePush() {
        if (deviceUuid.isEmpty()) deviceUuid = UUID.randomUUID().toString()
        val pushSubscriberData = getPushSubscriberModel(
                mindBoxOperationType = MindBoxOperationType.SUBSCRIBE,
                brand = Build.BRAND,
                model = Build.MODEL
        )
        val mindBoxOperationModel = MindBoxOperationModel(
                BuildConfig.MINDBOX_ENDPOINT,
                MindBoxOperationType.SUBSCRIBE.value,
                deviceUuid,
                pushSubscriberData
        )
        disposable = mindBoxOperation.sendOperation(mindBoxOperationModel)
            .subscribe(
                {
                    Log.d(tag, "subscribing to push complete")
                    release()
                    pushSubscriberRegistered = true
                    onSuccess?.invoke()
                },
                {
                    Log.e(tag, "subscribing to push error: $it")
                }
            )
    }
}
