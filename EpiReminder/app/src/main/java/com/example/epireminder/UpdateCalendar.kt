package com.example.epireminder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ParseException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.provider.CalendarContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import quicktype.Activity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*




class UpdateCalendar : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_calendar)
        var hubButton = findViewById<Button>(R.id.hubbutton)
        var calendarButton = findViewById<Button>(R.id.CalendarButton)
        var pressButton = findViewById<Button>(R.id.UpadateButton)
        realm = Realm.getDefaultInstance()
        var person = realm.where(Profile::class.java).findFirst()!!

        hubButton.setOnClickListener {
            val intent = Intent(applicationContext, Hub::class.java)
            startActivity(intent)
        }
        calendarButton.setOnClickListener{
            val calendarUri = CalendarContract.CONTENT_URI
                .buildUpon()
                .appendPath("time")
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, calendarUri))
        }
        pressButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                accessCalendar()
            }
            getActivity()
        }
    }

    fun getActivity() {
        // Instantiate the RequestQueue.
        realm = Realm.getDefaultInstance()
        val current = LocalDateTime.now()
        val passmonth = current.plusMonths(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedC = current.format(formatter)
        val formattedA = passmonth.format(formatter)
        var person = realm.where(Profile::class.java).findFirst()!!
        val queue = Volley.newRequestQueue(this)
        //WorkShop
        val url: String = "https://intra.epitech.eu/" + person.autolog + "/planning/load?format=json&start=" + formattedC.toString() + "&end=" + formattedA.toString()

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                var strResp = response.toString()
                Log.i("testt", strResp)
                val activities = Activity.fromJson(strResp)
                for (activity in activities) {
                    if (activity.eventRegistered!!.toJson() == "\"registered\"") {
                        Log.i("testt", activity.end)
                        Log.i("testt", SimpleDateFormat("yyyy-MM-DD HH:mm:ss").parse(activity.end).time.toString())
                        val calendarIntent: Intent = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(activity.start).time)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(activity.end).time)
                            .putExtra(CalendarContract.Events.TITLE, activity.actiTitle + " " + activity.room!!.code)
                            .putExtra(CalendarContract.Events.DESCRIPTION, "")
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                        this.startActivity(calendarIntent)
                    }
                }
            },
            Response.ErrorListener { Log.i("Error","That didn't work!") })
        queue.add(stringReq)
    }

    private fun accessCalendar() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR),
            8008)
    }

}
