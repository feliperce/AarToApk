package shared.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath


fun createDataStore(
    producePath: () -> String,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = null,
    migrations = emptyList(),
    produceFile = { producePath().toPath() },
)

class AppSettings(private val dataStore: DataStore<Preferences>) {

    val settingsData: Flow<SettingsData> =
        dataStore.data.map { preferences ->
            SettingsData(
                adbPath = preferences[ADB_PATH_PREF_KEY] ?: "",
                buildToolsPath = preferences[ADB_PATH_PREF_KEY] ?: "",
                outputPath = preferences[ADB_PATH_PREF_KEY] ?: ""
            )
        }

    suspend fun updateAdbPath(adbPath: String) {
        dataStore.edit { preferences ->
            preferences[ADB_PATH_PREF_KEY] = adbPath
        }
    }

    suspend fun updateBuildToolsPath(buildToolsPath: String) {
        dataStore.edit { preferences ->
            preferences[BUILD_TOOLS_PATH_PREF_KEY] = buildToolsPath
        }
    }

    suspend fun updateOutputPath(outputPath: String) {
        dataStore.edit { preferences ->
            preferences[OUTPUT_PATH_PREF_KEY] = outputPath
        }
    }

    suspend fun updateSettingsData(settingsData: SettingsData) {
        dataStore.edit { preferences ->
            preferences[ADB_PATH_PREF_KEY] = settingsData.adbPath
            preferences[BUILD_TOOLS_PATH_PREF_KEY] = settingsData.buildToolsPath
            preferences[OUTPUT_PATH_PREF_KEY] = settingsData.outputPath
        }
    }

    companion object {
        val ADB_PATH_PREF_KEY = stringPreferencesKey("adbPath")
        val BUILD_TOOLS_PATH_PREF_KEY = stringPreferencesKey("buildToolsPath")
        val OUTPUT_PATH_PREF_KEY = stringPreferencesKey("outputPath")
    }
}