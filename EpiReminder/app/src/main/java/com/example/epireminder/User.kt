// To parse the JSON, install Klaxon and do:
//
//   val user = User.fromJson(jsonString)

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
    .convert(JsonObject::class, { it.obj!! }, { it.toJsonString() })

data class User (
    val login: String,
    val title: String,

    @Json(name = "internal_email")
    val internalEmail: String,

    val lastname: String,
    val firstname: String,
    val userinfo: Rights,

    @Json(name = "referent_used")
    val referentUsed: Boolean,

    val picture: String,

    @Json(name = "picture_fun")
    val pictureFun: Any? = null,

    val scolaryear: String,
    val promo: Long,
    val semester: Long,
    val location: String,
    val documents: String,
    val userdocs: Any? = null,
    val shell: Any? = null,
    val close: Boolean,
    val ctime: String,
    val mtime: String,

    @Json(name = "id_promo")
    val idPromo: String,

    @Json(name = "id_history")
    val idHistory: String,

    @Json(name = "course_code")
    val courseCode: String,

    @Json(name = "semester_code")
    val semesterCode: String,

    @Json(name = "school_id")
    val schoolID: String,

    @Json(name = "school_code")
    val schoolCode: String,

    @Json(name = "school_title")
    val schoolTitle: String,

    @Json(name = "old_id_promo")
    val oldIDPromo: String,

    @Json(name = "old_id_location")
    val oldIDLocation: String,

    val rights: Rights,
    val invited: Boolean,
    val studentyear: Long,
    val admin: Boolean,
    val editable: Boolean,
    val restrictprofiles: Boolean,
    val groups: List<Group>,
    val events: List<Any?>,
    val credits: Long,
    val gpa: List<Gpa>,
    val spice: Any? = null,
    val nsstat: Nsstat
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<User>(json)
    }
}

data class Gpa (
    val gpa: String,
    val cycle: String
)

data class Group (
    val title: String,
    val name: String,
    val count: Long
)

data class Nsstat (
    val active: Double,
    val idle: Long,

    @Json(name = "out_active")
    val outActive: Long,

    @Json(name = "out_idle")
    val outIdle: Long,

    @Json(name = "nslog_norm")
    val nslogNorm: Long
)

typealias Rights = JsonObject
