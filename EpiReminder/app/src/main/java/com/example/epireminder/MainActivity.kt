package com.example.epireminder

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import io.realm.kotlin.createObject
import quicktype.User

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var loginButton = findViewById<Button>(R.id.logButton)
        var intraButton = findViewById<Button>(R.id.autologButton)
        var autologText = findViewById<TextView>(R.id.autologText)
        loginButton.setOnClickListener {
            if (autologText.text == "")
                return@setOnClickListener

            realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm ->
                realm.deleteAll()
            }
            getUser(autologText.text.toString(), realm)
        }
        intraButton.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://intra.epitech.eu/admin/autolog")
            startActivity(openURL)
        }
    }
    fun getUser(autolog:String, realm : Realm) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://intra.epitech.eu/" + autolog.subSequence(25, autolog.length).toString() + "/user/?format=json"

        // Request a string response from the provided URL.
        var stu: Profile
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                val user = User.fromJson(strResp)
                Log.i("test", strResp)
                realm.beginTransaction()
                stu = Profile(
                    email = user!!.internalEmail,
                    year = user!!.scolaryear,
                    autolog = autolog.subSequence(25, autolog.length).toString())
                realm.copyToRealmOrUpdate(stu)
                realm.commitTransaction()
                val intent = Intent(applicationContext, UpdateCalendar::class.java)
                startActivity(intent)
            },
            Response.ErrorListener {Log.i("Error","That didn't work!") })
        queue.add(stringReq)
    }
}
