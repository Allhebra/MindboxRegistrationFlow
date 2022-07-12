package com.example.mindboxregistrationflow.state.registrationactions

import android.util.Log
import com.example.mindboxregistrationflow.state.PushRegistrationAction
import com.example.mindboxregistrationflow.state.PushUtil.firebaseToken
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.core.inject
import org.koin.core.KoinComponent

class RetrieveFbTokenAction : PushRegistrationAction(), KoinComponent {
    private val tag = "RetrieveFbToken"
    private val firebaseCrashlytics: FirebaseCrashlytics by inject()

    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        val firebaseMessagingInstance = FirebaseMessaging.getInstance()
        firebaseMessagingInstance.token.addOnCompleteListener {
            try {
                val resultToken: String = it.result ?: ""
                Log.d(tag, "FirebaseMessaging token: $resultToken")
                firebaseToken = resultToken
                if (firebaseToken.isNotEmpty()) onSuccess?.invoke()
            } catch (t: Throwable) {
                firebaseCrashlytics.log("E/$tag: FirebaseMessaging token: failed to get")
                firebaseCrashlytics.recordException(t)
                Log.d(tag, "FirebaseMessaging token: failed to get")
            }
        }
    }
}
