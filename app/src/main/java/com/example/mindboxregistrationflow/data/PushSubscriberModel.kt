package com.example.mindboxregistrationflow.data

data class PushSubscriberModel(
        val customer: CustomerModel,
        val smsMailing: SmsMailingModel?,
        val customerAction: CustomerActionModel?,
        val mobileApplicationInstallation: AppInstallationDataModel?
)

data class CustomerModel(
        val subscriptions: List<PointOfContactModel>?,
        val ids: IdsModel?,
        val mobilePhone: String?
)

data class PointOfContactModel(
        val pointOfContact: String
)

data class IdsModel(
        val clientIdentificator: String?
)

data class AppInstallationDataModel(
        val id: String,
        val vendor: String,
        val model: String
)

data class SmsMailingModel(
        val customParameters: CustomParametersModel
)

data class CustomParametersModel(
        val passwordCode: String
)

data class CustomerActionModel(
        val customFields: CustomerFieldsModel
)

data class CustomerFieldsModel(
        val information: String
)
