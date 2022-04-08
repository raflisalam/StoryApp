package com.raflisalam.storyapp.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserSession private constructor(private val sessionDataStore: DataStore<Preferences>) {

    suspend fun setSessionUser(userId: String, name: String, token: String) {
        sessionDataStore.edit { preferences ->
            preferences[USER_ID_SESSION] = userId
            preferences[USER_NAME] = name
            preferences[USER_TOKEN] = token
        }
    }

    fun getUserId(): Flow<String> {
        return sessionDataStore.data.map { preferences ->
            preferences[USER_ID_SESSION] ?: ""
//            preferences[USER_NAME] ?: ""
//            preferences[USER_TOKEN] ?: ""

        }
    }

//    val userIdSession: Flow<String> = sessionDataStore.data.map { preferences ->
//        preferences[USER_ID_SESSION] ?: ""
//    }
//
//    val userName: Flow<String> = sessionDataStore.data.map { preferences ->
//        preferences[USER_ID_SESSION] ?: ""
//    }
//
//    val userToken: Flow<String> = sessionDataStore.data.map { preferences ->
//        preferences[USER_ID_SESSION] ?: ""
//    }

    companion object {
        val USER_ID_SESSION = stringPreferencesKey("session")
        val USER_NAME = stringPreferencesKey("name")
        val USER_TOKEN = stringPreferencesKey("token")

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