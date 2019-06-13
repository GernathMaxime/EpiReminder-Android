package com.example.epireminder

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        var c = RealmConfiguration.Builder()
        c.deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(c.build())
    }
}