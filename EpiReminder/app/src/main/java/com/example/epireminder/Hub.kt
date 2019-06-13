package com.example.epireminder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_hub.*
import quicktype.User

class Hub : AppCompatActivity() {

    private lateinit var realm: Realm
    private var nbTalk = 0
    private var nbWork = 0
    private var nbShare = 0
    private var nbMaker = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hub)
        var updateViewButton = findViewById<Button>(R.id.UpdateButton)
        var talk3text = findViewById<TextView>(R.id.Talk3)
        var talk5text = findViewById<TextView>(R.id.Talk5)
        var talk8text = findViewById<TextView>(R.id.Talk8)
        var exp3text = findViewById<TextView>(R.id.Exp3)
        var exp5text = findViewById<TextView>(R.id.Exp5)
        var exp8text = findViewById<TextView>(R.id.Exp8)
        var fruiton3text = findViewById<TextView>(R.id.Fruiton3)
        var fruiton5text = findViewById<TextView>(R.id.Fruiton5)
        var fruiton8text = findViewById<TextView>(R.id.Fruiton8)
        var sharing5text = findViewById<TextView>(R.id.Sharing5)
        var sharing8text = findViewById<TextView>(R.id.Sharing8)
        var check3 = findViewById<ImageView>(R.id.check3)
        var check5 = findViewById<ImageView>(R.id.check5)
        var check8 = findViewById<ImageView>(R.id.check8)

        check3.alpha = 0F
        check5.alpha = 0F
        check8.alpha = 0F
        updateViewButton.setOnClickListener {
            val intent = Intent(applicationContext, UpdateCalendar::class.java)
            startActivity(intent)
        }

        getHub()
    }

    fun getHub() {
        // Instantiate the RequestQueue.
        realm = Realm.getDefaultInstance()
        var person = realm.where(Profile::class.java).findFirst()!!
        val queue = Volley.newRequestQueue(this)
        //WorkShop
        val url: String = "https://intra.epitech.eu/" + person.autolog + "/module/"+ person.year + "/B-INN-000/PAR-0-1/?format=json"

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                Log.i("test", strResp)
                val workshop = HubTalk.fromJson(strResp)
                for (activityHub in workshop!!.activites) {
                    if (activityHub.title.contains(person.email, ignoreCase = true))
                        nbShare += 1
                    for (eventHub in activityHub.events) {
                        if (eventHub.userStatus == "present")
                            nbWork += 1
                    }
                }
                updateView()
            },
            Response.ErrorListener { Log.i("Error","That didn't work!") })
        queue.add(stringReq)

        val queue1 = Volley.newRequestQueue(this)
        //Talk
        val url1: String = "https://intra.epitech.eu/" + person.autolog + "/module/"+ person.year + "/B-INN-001/PAR-0-1/?format=json"

        // Request a string response from the provided URL.
        val stringReq1 = StringRequest(
            Request.Method.GET, url1,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                Log.i("test", strResp)
                val hubTalk = HubTalk.fromJson(strResp)
                for (activityHub in hubTalk!!.activites) {
                    if (activityHub.title.contains(person.email, ignoreCase = true))
                        nbShare += 1
                    for (eventHub in activityHub.events) {
                        if (eventHub.userStatus == "present")
                            nbTalk += 1
                    }
                }
                updateView()
            },
            Response.ErrorListener { Log.i("Error","That didn't work!") })
        queue1.add(stringReq1)
    }

    fun updateView() {
        this.Talk3.text = nbTalk.toString() + "/4"
        this.Talk5.text = nbTalk.toString() + "/4"
        this.Talk8.text = nbTalk.toString() + "/4"
        this.Exp3.text = nbWork.toString() + "/3"
        this.Exp5.text = nbWork.toString() + "/3"
        this.Exp8.text = nbWork.toString() + "/3"
        this.Sharing5.text = nbShare.toString() + "/1"
        this.Sharing8.text = nbShare.toString() + "/2"
        this.Fruiton3.text = nbMaker.toString() + "/1"
        this.Fruiton5.text = nbMaker.toString() + "/1"
        this.Fruiton8.text = nbMaker.toString() + "/2"
        if (nbTalk >= 4 && nbWork >= 3 && nbMaker >= 1) {
            this.check3.alpha = 1F
        }
        if (nbTalk >= 4 && nbWork >= 3 && nbShare >= 1 && nbMaker >= 1) {
            this.check5.alpha = 1F
        }
        if (nbTalk >= 4 && nbWork >= 3 && nbShare >= 2 && nbMaker >= 2) {
            this.check8.alpha = 1F
        }
    }
}
