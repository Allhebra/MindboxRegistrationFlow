package com.example.mindboxregistrationflow.data

data class AzureRequest(
        val hubName: String,
        val installationId: String,
        val azureInitializationData: AzureInitializationData
)
