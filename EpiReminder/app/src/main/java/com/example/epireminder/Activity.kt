// To parse the JSON, install Klaxon and do:
//
//   val activity = Activity.fromJson(jsonString)

package quicktype

import com.beust.klaxon.*

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()
    .convert(EventRegistered::class, { EventRegistered.fromJson(it) }, { it.toJson() }, true)

class Activity(elements: Collection<ActivityElement>) : ArrayList<ActivityElement>(elements) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = Activity(klaxon.parseArray<ActivityElement>(json)!!)
    }
}

data class ActivityElement (
    val scolaryear: String? = null,

    @Json(name = "acti_title")
    val actiTitle: String? = null,

    val start: String? = null,
    val end: String? = null,
    val room: Room? = null,

    @Json(name = "event_registered")
    val eventRegistered: EventRegistered? = null
)

sealed class EventRegistered {
    class BoolValue(val value: Boolean)  : EventRegistered()
    class StringValue(val value: String) : EventRegistered()

    public fun toJson(): String = klaxon.toJsonString(when (this) {
        is BoolValue   -> this.value
        is StringValue -> this.value
    })

    companion object {
        public fun fromJson(jv: JsonValue): EventRegistered = when (jv.inside) {
            is Boolean -> BoolValue(jv.boolean!!)
            is String  -> StringValue(jv.string!!)
            else       -> throw IllegalArgumentException()
        }
    }
}

data class Room (
    val code: String? = null
)
