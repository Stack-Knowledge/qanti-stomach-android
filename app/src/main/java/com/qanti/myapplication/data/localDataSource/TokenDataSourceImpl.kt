package com.qanti.myapplication.data.localDataSource

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "token_preferences")

class TokenDataSourceImpl @Inject constructor(
    private val context: Context
) : TokenDataSource {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val ID_KEY = stringPreferencesKey("id_key")
    }

    override suspend fun getToken(): UUID? {
        val tokenString = context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull() ?: ""

        return if (tokenString.isNotEmpty() && tokenString.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))) {
            UUID.fromString(tokenString)
        } else {
            null
        }
    }


    override suspend fun setToken(token: UUID) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token.toString()
        }
    }

    override suspend fun setUserId(id: Int) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id.toString()
        }
    }

    override suspend fun getUserId(): Int? {
        val userIdString = context.dataStore.data.map { preferences ->
            preferences[ID_KEY]
        }.firstOrNull() ?: ""

        return userIdString?.toIntOrNull()

    }


}