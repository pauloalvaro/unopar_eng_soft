package com.manus.lara.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePrefsManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveApiKey(apiKey: String) {
        sharedPreferences.edit().putString("GEMINI_API_KEY", apiKey).apply()
    }

    fun getApiKey(): String? {
        return sharedPreferences.getString("GEMINI_API_KEY", null)
    }

    fun clearApiKey() {
        sharedPreferences.edit().remove("GEMINI_API_KEY").apply()
    }

    fun isOnboardingComplete(): Boolean {
        return sharedPreferences.getBoolean("ONBOARDING_COMPLETE", false)
    }

    fun setOnboardingComplete(complete: Boolean) {
        sharedPreferences.edit().putBoolean("ONBOARDING_COMPLETE", complete).apply()
    }
}
