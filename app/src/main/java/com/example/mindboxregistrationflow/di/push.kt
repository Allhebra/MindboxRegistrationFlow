package com.example.mindboxregistrationflow.di

import com.example.mindboxregistrationflow.state.PushRegistrationAction
import com.example.mindboxregistrationflow.state.registrationactions.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val retrieveFbTokenActionQualifier = named("RetrieveFbTokenAction")
val mindBoxInitActionQualifier = named("MindBoxInitAction")
val sendTokenToAzureActionQualifier = named("SendTokenToAzureAction")
val subscribeToMobilePushActionQualifier = named("SubscribeToMobilePushAction")
val authorizePushCustomerActionQualifier = named("AuthorizePushCustomerAction")
val resetRegistrationStateActionQualifier = named("ResetRegistrationStateAction")

val pushRegistrationActionsModule = module {
    single<PushRegistrationAction>(retrieveFbTokenActionQualifier) { RetrieveFbTokenAction() }
    single<PushRegistrationAction>(mindBoxInitActionQualifier) { MindBoxInitAction() }
    single<PushRegistrationAction>(sendTokenToAzureActionQualifier) { SendTokenToAzureAction() }
    single<PushRegistrationAction>(subscribeToMobilePushActionQualifier) { SubscribeToMobilePushAction() }
    single<PushRegistrationAction>(authorizePushCustomerActionQualifier) { AuthorizePushCustomerAction() }
    single<PushRegistrationAction>(resetRegistrationStateActionQualifier) { ResetRegistrationStateAction() }
}
