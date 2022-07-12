package com.example.mindboxregistrationflow.state

interface PushRegistrationStateHolder {
    fun proceed()
    fun onNewFbToken(token: String)
    fun resetCustomerAuthorized()
}
