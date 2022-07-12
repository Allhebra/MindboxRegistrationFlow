package com.example.mindboxregistrationflow.state.registrationactions

import com.example.mindboxregistrationflow.PreferenceUtils
import com.example.mindboxregistrationflow.state.Init
import com.example.mindboxregistrationflow.state.PushRegistrationAction
import com.example.mindboxregistrationflow.state.PushRegistrationStateHolderImpl.Companion.PUSH_REGISTRATION_STATE_PREF_KEY
import org.koin.core.KoinComponent

class ResetRegistrationStateAction : PushRegistrationAction(), KoinComponent {
    override fun perform(param: Any?, onSuccess: (() -> Unit)?) {
        PreferenceUtils.set(PUSH_REGISTRATION_STATE_PREF_KEY, Init.name)
    }
}
