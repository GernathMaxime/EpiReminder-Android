package com.example.epireminder

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon

// To parse the JSON, install Klaxon and do:
//
//   val hubTalk = HubTalk.fromJson(jsonString)

private val klaxon = Klaxon()

data class HubTalk (
    val activites: List<Activite>
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<HubTalk>(json)
    }
}

data class Activite (
    val title: String,
    val events: List<Event>
)

data class Event (
    @Json(name = "user_status")
    val userStatus: Any? = null
)
