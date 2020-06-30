package com.rizalfahrudin.submission.viewmodel

import User
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserDetailViewModel : ViewModel() {
    val dataDetail = MutableLiveData<User>()

    internal fun getUserDetail(): LiveData<User> = dataDetail

    internal fun setUserDetail(username: String) {
        val client = AsyncHttpClient()
        val token = ""
        val url = "https://api.github.com/users/$username"

//        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val user = JSONObject(result)

                    val item = User(
                        user.getLong("id").toInt(),
                        user.getString("login"),
                        user.getString("name"),
                        user.getString("avatar_url"),
                        user.getString("company"),
                        user.getString("location"),
                        user.getInt("public_repos")
                    )
                    dataDetail.postValue(item)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }
}