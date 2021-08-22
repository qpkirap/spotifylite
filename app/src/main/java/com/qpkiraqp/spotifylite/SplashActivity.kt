package com.qpkiraqp.spotifylite

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.qpkiraqp.spotifylite.model.entities.User


class SplashActivity : AppCompatActivity() {

    private var editor: SharedPreferences.Editor? = null
    private var msharedPreferences: SharedPreferences? = null
    private var queue: RequestQueue? = null
    private var user: User? = null
    private val SCOPES =
        "user-read-recently-played,user-library-modify,user-read-email,user-read-private"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }


    override fun onStart() {
        super.onStart()
        SpotifyService.connect(this) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //msharedPreferences = getSharedPreferences("SPOTIFY", 0);
            //queue = Volley.newRequestQueue(this);
            //waitForUserInfo();
        }
    }


    private fun waitForUserInfo() {
        val userService = UserService(queue, msharedPreferences)
        userService[object : VolleyCallBack {
            override fun onSuccess() {
                user = userService.getUser()
            }
        }]
    }





}