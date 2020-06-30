package com.rizalfahrudin.submission.view

import User
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rizalfahrudin.submission.R
import com.rizalfahrudin.submission.adapter.UserFollAdapter
import com.rizalfahrudin.submission.db.DatabaseContract
import com.rizalfahrudin.submission.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.rizalfahrudin.submission.db.UserHelper
import com.rizalfahrudin.submission.helper.MappingHelper
import com.rizalfahrudin.submission.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "data"
    }

    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var userFollAdapter: UserFollAdapter
    private lateinit var userHelper: UserHelper
    private lateinit var userData: User
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        val userDataIntent = intent.getParcelableExtra(EXTRA_DATA) as User

        userDetailViewModel(userDataIntent.login!!)

        userFollAdapter = UserFollAdapter(this, userDataIntent.login!!)
        view_pager.adapter = userFollAdapter

        val tabTitle = resources.getStringArray(R.array.tab_title)

        TabLayoutMediator(tab_detail, view_pager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        loadDataSQLite(userDataIntent.id!!)
    }

    private fun loadDataSQLite(id: Int) {
        uriWithId = Uri.parse("$CONTENT_URI/$id")
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            updateFavorite(MappingHelper.mapCursorToArrayList(cursor), id)
            cursor.close()
        }
    }

    private fun updateFavorite(users: ArrayList<User>, id: Int) {

        if (users.size > 0) btn_favorite.setImageResource(R.drawable.baseline_clear_white_48dp)
        else btn_favorite.setImageResource(R.drawable.baseline_favorite_white_18dp)

        btn_favorite.setOnClickListener {
            if (users.size > 0) {
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(
                    this@UserDetailActivity,
                    "Berhasil hapus dari favorite",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val value = ContentValues()
                value.put(DatabaseContract.UserColumn._ID, userData.id)
                value.put(DatabaseContract.UserColumn.USERNAME, userData.login)
                value.put(DatabaseContract.UserColumn.NAME, userData.name)
                value.put(DatabaseContract.UserColumn.AVA, userData.avatarURL)
                value.put(DatabaseContract.UserColumn.COMPANY, userData.company)
                value.put(DatabaseContract.UserColumn.LOCATION, userData.location)
                value.put(DatabaseContract.UserColumn.REPOS, userData.repos)
                contentResolver.insert(CONTENT_URI, value)
                Toast.makeText(
                    this@UserDetailActivity,
                    "Berhasil ditambahkan ke favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
            loadDataSQLite(id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun userDetailViewModel(username: String) {
        userDetailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserDetailViewModel::class.java)
        userDetailViewModel.setUserDetail(username)

        userDetailViewModel.getUserDetail().observe(this, Observer {
            if (it != null) {
                this.userData = it
                Glide.with(this)
                    .load(it.avatarURL)
                    .into(img_ava_detail)
                tv_name_detail.text = it.name
                tv_username_detail.text = it.login
                tv_info_detail.text = getString(
                    R.string.info_detail,
                    if (it.company == "null") "-" else it.company,
                    if (it.location == "null") "-" else it.location
                )
            }
            tv_repositories_detail.text = getString(R.string.repositories, it.repos.toString())
        })
    }
}