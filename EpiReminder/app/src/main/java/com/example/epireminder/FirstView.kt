package com.example.epireminder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm

class FirstView : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_view)
        realm = Realm.getDefaultInstance()
        var person = realm.where(Profile::class.java).findFirst()

        if (person == null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(applicationContext, UpdateCalendar::class.java)
            startActivity(intent)
        }
    }
}
