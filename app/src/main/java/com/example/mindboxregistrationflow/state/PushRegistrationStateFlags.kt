package com.example.mindboxregistrationflow.state

import com.example.mindboxregistrationflow.PreferenceUtils

var azureRegistrationComplete: Boolean
    get() = PreferenceUtils.getBoolean(AZURE_REGISTRATION_COMPLETE_PREF_KEY)
    set(value) {
        PreferenceUtils.set(AZURE_REGISTRATION_COMPLETE_PREF_KEY, value)
    }

var pushSubscriberRegistered: Boolean
    get() = PreferenceUtils.getBoolean(PUSH_SUBSCRIBER_REGISTERED_PREF_KEY)
    set(value) {
        PreferenceUtils.set(PUSH_SUBSCRIBER_REGISTERED_PREF_KEY, value)
    }

var customerAuthorized: Boolean
    get() = PreferenceUtils.getBoolean(CUSTOMER_AUTHORIZED_PREF_KEY)
    set(value) {
        PreferenceUtils.set(CUSTOMER_AUTHORIZED_PREF_KEY, value)
    }

private const val AZURE_REGISTRATION_COMPLETE_PREF_KEY = "AZURE_REGISTRATION_COMPLETE_PREF_KEY"
private const val PUSH_SUBSCRIBER_REGISTERED_PREF_KEY = "PUSH_SUBSCRIBER_REGISTERED_PREF_KEY"
private const val CUSTOMER_AUTHORIZED_PREF_KEY = "CUSTOMER_AUTHORIZED_PREF_KEY"
