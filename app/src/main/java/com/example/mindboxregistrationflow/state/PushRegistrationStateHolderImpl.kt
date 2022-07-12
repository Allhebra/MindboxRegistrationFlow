package com.example.mindboxregistrationflow.state

import com.example.mindboxregistrationflow.PreferenceUtils
import com.example.mindboxregistrationflow.state.PushRegistrationState.Companion.fromString
import com.example.mindboxregistrationflow.state.PushUtil.firebaseToken

class PushRegistrationStateHolderImpl : PushRegistrationStateHolder {
    private var currentState: PushRegistrationState
        get() {
            val state = PreferenceUtils.getString(PUSH_REGISTRATION_STATE_PREF_KEY, Init.name)
            return fromString(state)
        }
        set(value) {
            PreferenceUtils.set(PUSH_REGISTRATION_STATE_PREF_KEY, value.name)
        }

    override fun proceed() {
        proceedInternal()
    }

    override fun onNewFbToken(token: String) {
        firebaseToken = token
        azureRegistrationComplete = false
        currentState = Init
        proceedInternal()
    }

    override fun resetCustomerAuthorized() {
        customerAuthorized = false
    }

    @Synchronized
    private fun proceedInternal() {
        currentState.performAction()
    }

    companion object {
        const val PUSH_REGISTRATION_STATE_PREF_KEY = "PUSH_REGISTRATION_STATE_PREF_KEY"
    }
}
