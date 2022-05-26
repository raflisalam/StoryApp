package com.raflisalam.storyapp.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSession private constructor(private val sessionDataStore: DataStore<Preferences>) {

    suspend fun setSessionUser(name: String, token: String, session: Boolean) {
        sessionDataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_TOKEN] = token
            preferences[USER_SESSION] = session
        }
    }

    fun getUserSession(): Flow<Boolean> {
        return sessionDataStore.data.map { preferences ->
            preferences[USER_SESSION] ?: false
        }
    }

    val userToken: Flow<String> = sessionDataStore.data.map { preferences ->
        preferences[USER_TOKEN] ?: ""
    }

    companion object {
        val USER_NAME = stringPreferencesKey("name")
        val USER_TOKEN = stringPreferencesKey("token")
        val USER_SESSION = booleanPreferencesKey("session")

        @Volatile
        private var instance: UserSession? = null

        fun newInstance(dataStore: DataStore<Preferences>) : UserSession {
            return instance ?: synchronized(this) {
                val ins = UserSession(dataStore)
                instance = ins
                ins
            }
        }
    }
}