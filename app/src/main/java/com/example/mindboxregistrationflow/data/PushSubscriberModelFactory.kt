package com.example.mindboxregistrationflow.data

import com.example.mindboxregistrationflow.state.PushUtil
import java.lang.IllegalArgumentException

fun getPushSubscriberModel(
    mindBoxOperationType: MindBoxOperationType,
    clientIdentificator: String = "",
    brand: String,
    model: String
) : PushSubscriberModel {
    return when (mindBoxOperationType) {
        MindBoxOperationType.SUBSCRIBE -> PushSubscriberModel(
                customer = createAnonymousCustomer(),
                smsMailing = null,
                customerAction = null,
                mobileApplicationInstallation = createAppInstallationData(brand, model)
        )
        MindBoxOperationType.AUTHORIZE_CUSTOMER -> PushSubscriberModel(
                customer = createUserCustomer(clientIdentificator),
                smsMailing = null,
                customerAction = null,
                mobileApplicationInstallation = createAppInstallationData(brand, model)
        )
        MindBoxOperationType.CODE_RECOVERY -> PushSubscriberModel(
                customer = createUserCustomer(clientIdentificator),
                smsMailing = null,//todo
                customerAction = null,//todo
                mobileApplicationInstallation = createAppInstallationData(brand, model)
        )
        MindBoxOperationType.ACCIDENT_CLOSURE -> PushSubscriberModel(
                customer = createUserCustomer(clientIdentificator),
                smsMailing = null,//todo
                customerAction = null,//todo
                mobileApplicationInstallation = null
        )
    }
}

fun createAnonymousCustomer() = CustomerModel(
        subscriptions = listOf(PointOfContactModel("Mobilepush")),
        ids = IdsModel(null),
        mobilePhone = null
)

fun createUserCustomer(clientIdentificator: String) : CustomerModel {
    if (clientIdentificator.isEmpty())
        throw IllegalArgumentException("clientIdentificator must not be empty")
    return CustomerModel(
            subscriptions = null,
            ids = IdsModel(clientIdentificator),
            mobilePhone = null
    )
}

fun createAppInstallationData(brand: String, model: String) = AppInstallationDataModel(
        PushUtil.installationId,
        brand,
        model
)
