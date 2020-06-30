package com.rizalfahrudin.favoriteapp.view

import User
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalfahrudin.favoriteapp.R
import com.rizalfahrudin.favoriteapp.adapter.UserAdapter
import com.rizalfahrudin.favoriteapp.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.rizalfahrudin.favoriteapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "List User Favorite"

        showRecyclerListViewUser()
        loadUserFavoritesAsync()

        if (savedInstanceState == null) {
            loadUserFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.mUser = list
            }
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val mObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserFavoritesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, mObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.mUser)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadUserFavoritesAsync() {
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

    private fun showRecyclerListViewUser() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_users_favorite.layoutManager = LinearLayoutManager(this)
        rv_users_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val mIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                mIntent.putExtra(UserDetailActivity.EXTRA_DATA, data)
                startActivity(mIntent)
            }
        })

    }

}
