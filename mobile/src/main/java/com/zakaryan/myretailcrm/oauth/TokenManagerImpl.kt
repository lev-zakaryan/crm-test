package com.zakaryan.myretailcrm.oauth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.zakaryan.myretailcrm.oauth.model.TokenCache

class TokenManagerImpl(context: Context) : TokenManager {

    private val mainKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPrefs =
        EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCES_FILE,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    override fun saveTokenCache(cache: TokenCache.Access) {
        val (url, access, refresh) = cache
        with(sharedPrefs.edit()) {
            putString(PREF_NAME_CLIENT_URL, url)
            putString(PREF_NAME_TOKEN_ACCESS, access)
            putString(PREF_NAME_TOKEN_REFRESH, refresh)
            apply()
        }
    }

    override fun readTokenCache(): TokenCache {
        val url = readClientUrl()
        val access = readAccessToken()
        val refresh = readRefreshToken()
        return if (url.isNullOrBlank() || access.isNullOrBlank() || refresh.isNullOrBlank()) {
            TokenCache.Empty
        } else {
            TokenCache.Access(url, access, refresh)
        }
    }

    override fun clearTokenCache() {
        with(sharedPrefs.edit()) {
            remove(PREF_NAME_CLIENT_URL)
            remove(PREF_NAME_TOKEN_ACCESS)
            remove(PREF_NAME_TOKEN_REFRESH)
            apply()
        }
    }

    private fun readClientUrl(): String? {
        return sharedPrefs.getString(PREF_NAME_CLIENT_URL, null)
    }

    private fun readAccessToken(): String? {
        return sharedPrefs.getString(PREF_NAME_TOKEN_ACCESS, null)
    }

    private fun readRefreshToken(): String? {
        return sharedPrefs.getString(PREF_NAME_TOKEN_REFRESH, null)
    }

    /* ---------------------------- COMPANION --------------------------------------------------- */

    companion object {
        private const val SHARED_PREFERENCES_FILE = "CRM_SHARED_PREFS"
        private const val PREF_NAME_CLIENT_URL = "PREFERENCE_CLIENT_URL"
        private const val PREF_NAME_TOKEN_ACCESS = "PREF_NAME_TOKEN_ACCESS"
        private const val PREF_NAME_TOKEN_REFRESH = "PREFERENCE_REFRESH_TOKEN"
    }
}