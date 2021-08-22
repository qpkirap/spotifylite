package com.qpkiraqp.spotifylite

import android.content.SharedPreferences
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.qpkiraqp.spotifylite.model.entities.User
import org.json.JSONObject


class UserService(queue: RequestQueue?, sharedPreferences: SharedPreferences?)
{
    private val ENDPOINT = "https://api.spotify.com/v1/me"
    private var msharedPreferences: SharedPreferences? = sharedPreferences
    private var mqueue: RequestQueue? = queue
    private var user: User? = null

    operator fun get(callBack: VolleyCallBack) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(ENDPOINT, null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                user = gson.fromJson(
                    response.toString(),
                    User::class.java
                )
                callBack.onSuccess()
            },
            Response.ErrorListener {
                get(object : VolleyCallBack {
                override fun onSuccess() {

                }
            }) }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val token = msharedPreferences!!.getString("token", "")
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }
        }
        mqueue!!.add(jsonObjectRequest)
    }

    fun getUser(): User? {
        return user
    }



}