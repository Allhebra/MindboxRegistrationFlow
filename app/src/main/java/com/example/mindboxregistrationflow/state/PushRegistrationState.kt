package com.example.mindboxregistrationflow.state

import android.accounts.Account
import com.example.mindboxregistrationflow.PreferenceUtils
import com.example.mindboxregistrationflow.di.*
import com.example.mindboxregistrationflow.state.PushRegistrationStateHolderImpl.Companion.PUSH_REGISTRATION_STATE_PREF_KEY
import com.example.mindboxregistrationflow.state.PushUtil.firebaseToken
import com.example.mindboxregistrationflow.state.PushUtil.hasInstallationData
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import org.koin.core.inject

sealed class PushRegistrationState : KoinComponent {
    val name: String = this::class.java.simpleName

    abstract fun performAction()
    abstract val action: PushRegistrationAction

    fun performNextState(newState: PushRegistrationState) {
        PreferenceUtils.set(PUSH_REGISTRATION_STATE_PREF_KEY, newState.name)
        newState.performAction()
    }

    companion object {
        fun fromString(name: String): PushRegistrationState {
            return when (name) {
                Init::class.java.simpleName -> Init
                FbTokenAppointed::class.java.simpleName -> FbTokenAppointed()
                InstallationRegistered::class.java.simpleName -> InstallationRegistered()
                FbTokenRegistered::class.java.simpleName -> FbTokenRegistered
                PushSubscriberRegistered::class.java.simpleName -> PushSubscriberRegistered
                AuthorizationCompleted::class.java.simpleName -> AuthorizationCompleted
                else -> Init
            }
        }
    }
}

object Init : PushRegistrationState() {
    override val action: PushRegistrationAction by inject(retrieveFbTokenActionQualifier)

    override fun performAction() {
        if (firebaseToken.isEmpty()) {
            action.perform { performNextState(FbTokenAppointed()) }
        } else {
            performNextState(FbTokenAppointed())
        }
    }
}

class FbTokenAppointed : PushRegistrationState() {
    override val action: PushRegistrationAction by inject(mindBoxInitActionQualifier)

    override fun performAction() {
        if (hasInstallationData()) performNextState(InstallationRegistered())
        else action.perform { performNextState(InstallationRegistered()) }
    }
}

class InstallationRegistered : PushRegistrationState() {
    override val action: PushRegistrationAction by inject(sendTokenToAzureActionQualifier)

    override fun performAction() {
        if (!azureRegistrationComplete && firebaseToken.isNotEmpty()) {
            action.perform(firebaseToken) { performNextState(FbTokenRegistered) }
        } else performNextState(FbTokenRegistered)
    }
}

object FbTokenRegistered : PushRegistrationState() {
    override val action: PushRegistrationAction by inject(subscribeToMobilePushActionQualifier)

    override fun performAction() {
        if (!pushSubscriberRegistered) action.perform { performNextState(PushSubscriberRegistered) }
        else performNextState(PushSubscriberRegistered)
    }
}

object PushSubscriberRegistered : PushRegistrationState() {
    private val account: Account by inject()
    override val action: PushRegistrationAction by inject(authorizePushCustomerActionQualifier)

    override fun performAction() {
        val contractId: String? = account.name
        if (!customerAuthorized && !contractId.isNullOrEmpty()) {
            action.perform(contractId) { performNextState(AuthorizationCompleted) }
        } else performNextState(AuthorizationCompleted)
    }
}

object AuthorizationCompleted : PushRegistrationState() {
    override val action: PushRegistrationAction by inject(resetRegistrationStateActionQualifier)

    override fun performAction() {
        action.perform()
    }
}

abstract class PushRegistrationAction {
    var disposable: Disposable? = null
    abstract fun perform(param: Any? = null, onSuccess: (() -> Unit)? = null)
    fun release() {
        disposable?.dispose()
        disposable = null
    }
}
