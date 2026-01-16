package com.ever.funquizz.model

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object PartySerializer {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, object : JsonDeserializer<Date> {
            override fun deserialize(
                json: JsonElement,
                typeOfT: Type,
                context: JsonDeserializationContext
            ): Date = Date(json.asJsonPrimitive.asLong)
        })
        .registerTypeAdapter(Date::class.java, object : JsonSerializer<Date> {
            override fun serialize(
                src: Date,
                typeOfSrc: Type,
                context: JsonSerializationContext
            ): JsonElement = JsonPrimitive(src.time)
        })
        .create()

    /* 1 objet */
    fun toJson(party: Party): String = gson.toJson(party)
    fun fromJson(json: String): Party = gson.fromJson(json, Party::class.java)

    /* Liste d’objets */
    fun toJsonList(list: List<Party>): String = gson.toJson(list)
    fun fromJsonList(json: String): List<Party> =
        gson.fromJson(json, object : TypeToken<List<Party>>() {}.type)
}