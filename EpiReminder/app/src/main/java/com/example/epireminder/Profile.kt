package com.example.epireminder

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Profile(
    @PrimaryKey open var id: Int = 1,
    open var email: String = "",
    open var autolog: String = "",
    open var year: String = ""
) : RealmObject() {
    fun copy(
        email: String = this.email,
        autolog: String = this.autolog,
        year: String = this.year)
            = Profile(id, email, autolog, year)
}