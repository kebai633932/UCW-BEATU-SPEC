package com.ucw.beatu.shared.database.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "beatu_preferences")

/**
 * DataStore管理器
 * 用于存储简单的键值对配置数据（如用户设置、token等）
 */
@Singleton
class PreferencesDataStore @Inject constructor(
    private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    // ========== String ==========
    suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getString(key: String, defaultValue: String = ""): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getStringSync(key: String, defaultValue: String = ""): String {
        return dataStore.data.first().let { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== Int ==========
    suspend fun putInt(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getIntSync(key: String, defaultValue: Int = 0): Int {
        return dataStore.data.first().let { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== Long ==========
    suspend fun putLong(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getLongSync(key: String, defaultValue: Long = 0L): Long {
        return dataStore.data.first().let { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== Float ==========
    suspend fun putFloat(key: String, value: Float) {
        dataStore.edit { preferences ->
            preferences[floatPreferencesKey(key)] = value
        }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getFloatSync(key: String, defaultValue: Float = 0f): Float {
        return dataStore.data.first().let { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== Boolean ==========
    suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getBooleanSync(key: String, defaultValue: Boolean = false): Boolean {
        return dataStore.data.first().let { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== StringSet ==========
    suspend fun putStringSet(key: String, value: Set<String>) {
        dataStore.edit { preferences ->
            preferences[stringSetPreferencesKey(key)] = value
        }
    }

    fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[stringSetPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun getStringSetSync(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
        return dataStore.data.first().let { preferences ->
            preferences[stringSetPreferencesKey(key)] ?: defaultValue
        }
    }

    // ========== 通用删除 ==========
    /**
     * 删除指定key的偏好设置
     * 注意：由于DataStore的key是类型特定的，此方法会尝试删除所有可能类型的key
     * 
     * 如果你知道key的具体类型，建议使用对应的类型化删除方法：
     * - removeString(key)
     * - removeInt(key)
     * - removeLong(key)
     * - removeFloat(key)
     * - removeBoolean(key)
     * - removeStringSet(key)
     */
    suspend fun remove(key: String) {
        // 先读取当前preferences，检查哪个类型的key实际存在
        val currentPreferences = dataStore.data.first()
        
        dataStore.edit { preferences ->
            // 检查并删除存在的key（按类型）
            val stringKey = stringPreferencesKey(key)
            if (currentPreferences.contains(stringKey)) {
                preferences.remove(stringKey)
            }
            
            val intKey = intPreferencesKey(key)
            if (currentPreferences.contains(intKey)) {
                preferences.remove(intKey)
            }
            
            val longKey = longPreferencesKey(key)
            if (currentPreferences.contains(longKey)) {
                preferences.remove(longKey)
            }
            
            val floatKey = floatPreferencesKey(key)
            if (currentPreferences.contains(floatKey)) {
                preferences.remove(floatKey)
            }
            
            val booleanKey = booleanPreferencesKey(key)
            if (currentPreferences.contains(booleanKey)) {
                preferences.remove(booleanKey)
            }
            
            val stringSetKey = stringSetPreferencesKey(key)
            if (currentPreferences.contains(stringSetKey)) {
                preferences.remove(stringSetKey)
            }
        }
    }

    /**
     * 类型化删除方法 - 删除String类型的key
     */
    suspend fun removeString(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }

    /**
     * 类型化删除方法 - 删除Int类型的key
     */
    suspend fun removeInt(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(intPreferencesKey(key))
        }
    }

    /**
     * 类型化删除方法 - 删除Long类型的key
     */
    suspend fun removeLong(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(longPreferencesKey(key))
        }
    }

    /**
     * 类型化删除方法 - 删除Float类型的key
     */
    suspend fun removeFloat(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(floatPreferencesKey(key))
        }
    }

    /**
     * 类型化删除方法 - 删除Boolean类型的key
     */
    suspend fun removeBoolean(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(booleanPreferencesKey(key))
        }
    }

    /**
     * 类型化删除方法 - 删除StringSet类型的key
     */
    suspend fun removeStringSet(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringSetPreferencesKey(key))
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

