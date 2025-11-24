package com.ucw.beatu.shared.database.converter

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
/**
 * Room 数据库类型转换器：解决 Room 不支持直接存储 List<String> 类型的问题
 * 核心逻辑：通过 Moshi 库将 List<String> 与 JSON 字符串互相转换
 * Room 仅支持基础数据类型（String/Int 等），集合类型需转换为字符串存储
 */
class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter = moshi.adapter<List<String>>(listType)

    @TypeConverter
    fun toJson(list: List<String>): String = adapter.toJson(list)

    @TypeConverter
    fun fromJson(json: String): List<String> = adapter.fromJson(json) ?: emptyList()
}

