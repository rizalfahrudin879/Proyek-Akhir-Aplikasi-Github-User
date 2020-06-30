package com.rizalfahrudin.submission.view

import User
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalfahrudin.submission.R
import com.rizalfahrudin.submission.adapter.UserAdapter
import com.rizalfahrudin.submission.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = resources.getString(R.string.app_name)

        setupViewModel()
        showRecyclerListViewUser()
    }

    private fun showRecyclerListViewUser() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val mIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                mIntent.putExtra(UserDetailActivity.EXTRA_DATA, data)
                startActivity(mIntent)
            }
        })
    }

    private fun setupViewModel() {
        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UserViewModel::class.java
        )

        edt_username.addTextChangedListener {
            userViewModel.setUserList(it.toString())
            showLoading(true)
        }

        userViewModel.getUserList().observe(this, Observer {
            if (it != null) adapter.mUser = it
            showLoading(false)
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_setting -> {
                val mIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(mIntent)
            }
            R.id.btn_favorite -> {
                val mIntent = Intent(this@MainActivity, UserFavoriteActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}