package com.example.mindboxregistrationflow.data

data class MindBoxOperationModel(
    val endpointId: String,
    val operation: String,
    val deviceUUID: String,
    val pushSubscriberModel: PushSubscriberModel
)

enum class MindBoxOperationType(val value: String) {
    SUBSCRIBE("Mobile.SubscribeToMobilePush"),
    AUTHORIZE_CUSTOMER("Mobile.AuthorizeCustomer"),
    CODE_RECOVERY("Mobile.CodeRecoveryPassword"),
    ACCIDENT_CLOSURE("AccidentClosureNotification")
}
