package com.rizalfahrudin.submission.view

import User
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalfahrudin.submission.R
import com.rizalfahrudin.submission.adapter.UserAdapter
import com.rizalfahrudin.submission.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.rizalfahrudin.submission.db.UserHelper
import com.rizalfahrudin.submission.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favorite)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        loadDataSQLite()
        showListData()
    }

    override fun onRestart() {
        super.onRestart()
        loadDataSQLite()
        showListData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadDataSQLite() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUser.await()

            if (users.size > 0) adapter.mUser = users
            else adapter.mUser = ArrayList()

        }
    }

    private fun showListData() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_users_favorite.layoutManager = LinearLayoutManager(this)
        rv_users_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val mIntent = Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
                mIntent.putExtra(UserDetailActivity.EXTRA_DATA, data)
                startActivity(mIntent)
            }

        })
    }

}