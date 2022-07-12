package com.example.mindboxregistrationflow.data

import com.fasterxml.jackson.annotation.JsonProperty

data class AzureInitializationData(
        val installationId: String,
        @JsonProperty("Platform")
        val platform: String = "gcm",
        val pushChannel: String
)
