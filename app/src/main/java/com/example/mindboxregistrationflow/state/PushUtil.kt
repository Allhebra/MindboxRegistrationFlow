package com.example.mindboxregistrationflow.state

import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.mindboxregistrationflow.PreferenceUtils
import com.example.mindboxregistrationflow.data.MindBoxInstallationData
import java.io.UnsupportedEncodingException
import java.lang.IllegalArgumentException
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object PushUtil {

    private const val DEVICE_UUID_PREF_KEY = "DEVICE_UUID_PREF_KEY"
    private const val FIREBASE_TOKEN_PREF_KEY = "FIREBASE_TOKEN_PREF_KEY"
    private const val INSTALLATION_ID_PREF_KEY = "INSTALLATION_ID_PREF_KEY"
    private const val HUB_NAME_PREF_KEY = "HUB_NAME_PREF_KEY"
    private const val CONNECTION_STRING_PREF_KEY = "CONNECTION_STRING_PREF_KEY"
    private const val SHARED_ACCESS_KEY_NAME_PREF_KEY = "SHARED_ACCESS_KEY_NAME_PREF_KEY"
    private const val SHARED_ACCESS_KEY_PREF_KEY = "SHARED_ACCESS_KEY_PREF_KEY"
    private const val sasTokenTemplate = "SharedAccessSignature sr=%s&sig=%s&se=%s&skn=%s"
    private const val azureBaseUrlTemplate = "https://%s.servicebus.windows.net/"
    private const val azurePathTemplate = "%s/installations/%s?api-version=2015-01"
    private const val WEEK = 60 * 60 * 24 * 7
    private val TAG = PushUtil::class.java.simpleName

    var deviceUuid = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(DEVICE_UUID_PREF_KEY, "")
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(DEVICE_UUID_PREF_KEY, value)
            field = value
        }

    var firebaseToken = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(FIREBASE_TOKEN_PREF_KEY, "")
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(FIREBASE_TOKEN_PREF_KEY, value)
            field = value
        }

    var installationId = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(INSTALLATION_ID_PREF_KEY, "")
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(INSTALLATION_ID_PREF_KEY, value)
            field = value
        }

    var hubName = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(HUB_NAME_PREF_KEY, "")
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(HUB_NAME_PREF_KEY, value)
            field = value
        }

    var connectionString = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(CONNECTION_STRING_PREF_KEY, "")
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(CONNECTION_STRING_PREF_KEY, value)
            field = value
        }

    private var resourceUri: String = ""
        get() = field.ifEmpty { createInstallationUrl().apply { field = this } }

    private var key: String = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(SHARED_ACCESS_KEY_PREF_KEY)
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(SHARED_ACCESS_KEY_PREF_KEY, value)
            field = value
        }

    private var keyName: String = ""
        get() {
            return field.ifEmpty {
                PreferenceUtils.getString(SHARED_ACCESS_KEY_NAME_PREF_KEY)
                    .apply { field = this }
            }
        }
        set(value) {
            PreferenceUtils.set(SHARED_ACCESS_KEY_NAME_PREF_KEY, value)
            field = value
        }

    fun getSASToken(): String {
        Log.d(TAG, "input data for SASToken calculating: $resourceUri $key $keyName")
        val expiry: String = (System.currentTimeMillis() / 1000L + WEEK).toString()
        var sasToken = ""
        try {
            val stringToSign = URLEncoder.encode(resourceUri, "UTF-8") + "\n" + expiry
            val signature = URLEncoder.encode(getHMAC256(key, stringToSign), "UTF-8")
            val resourceUrl = URLEncoder.encode(resourceUri, "UTF-8")
            sasToken = sasTokenTemplate.format(resourceUrl, signature, expiry, keyName)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        Log.d(TAG, "SAS token: $sasToken")
        return sasToken
    }

    private fun getHMAC256(key: String, input: String): String? {
        var hash: String? = null
        try {
            val sha256HMAC = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
            sha256HMAC.init(secretKey)
            hash = Base64.encodeToString(sha256HMAC.doFinal(input.toByteArray()), Base64.NO_WRAP)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return hash
    }

    fun createAzureBaseUrl(): String {
        val namespace = extractNamespace(connectionString)
        return azureBaseUrlTemplate.format(namespace)
    }

    private fun createInstallationUrl(): String {
        val namespace = extractNamespace(connectionString)
        Log.d(TAG, "connectionString: $connectionString")
        Log.d(TAG, "installationId: $installationId")
        Log.d(TAG, "namespace: $namespace")
        val installationUrl = azureBaseUrlTemplate.format(namespace) +
                azurePathTemplate.format(hubName, installationId)
        Log.d(TAG, "Azure installation url created: $installationUrl")
        return installationUrl
    }

    fun saveInstallationData(mindBoxInstallationData: MindBoxInstallationData) {
        installationId = mindBoxInstallationData.installationId
        hubName = mindBoxInstallationData.hubName
        connectionString = mindBoxInstallationData.connectionString
        keyName = extractSharedAccessKeyName(mindBoxInstallationData.connectionString)
        key = extractSharedAccessKey(mindBoxInstallationData.connectionString)
    }

    fun hasInstallationData(): Boolean {
        return installationId.isNotEmpty() && hubName.isNotEmpty() && connectionString.isNotEmpty()
    }

    private fun extractSharedAccessKeyName(connectionString: String) =
        connectionString.split(";")[1].replace("SharedAccessKeyName=", "")

    private fun extractSharedAccessKey(connectionString: String) =
        connectionString.split(";")[2].replace("SharedAccessKey=", "")

    private fun extractNamespace(connectionString: String): String {
        val endpoint = connectionString.split(";")[0]
        val endpointUri = endpoint.split("=")[1]
        val namespace = Uri.parse(endpointUri).host?.split(".")?.get(0)
            ?: throw IllegalArgumentException("Wrong connectionString")
        Log.d(TAG, "endpoint: $endpoint")
        Log.d(TAG, "endpointUri: $endpointUri")
        return namespace
    }
}
