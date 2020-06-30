package com.rizalfahrudin.favoriteapp.view

import User
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rizalfahrudin.favoriteapp.R
import com.rizalfahrudin.favoriteapp.adapter.UserFollAdapter
import com.rizalfahrudin.favoriteapp.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.rizalfahrudin.favoriteapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "data"
    }

    private lateinit var userFollAdapter: UserFollAdapter
    private lateinit var userData: User
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Detail User Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userDataIntent = intent.getParcelableExtra(EXTRA_DATA) as User


        userFollAdapter = UserFollAdapter(this, userDataIntent.login!!)
        view_pager.adapter = userFollAdapter

        val tabTitle = resources.getStringArray(R.array.tab_title)
        TabLayoutMediator(tab_detail, view_pager) { tab, posisi ->
            tab.text = tabTitle[posisi]
        }.attach()


        loadDataSQLite(userDataIntent.id!!)

        updateFavorite()
    }

    private fun loadDataSQLite(id: Int) {
        uriWithId = Uri.parse("$CONTENT_URI/$id")
        val cursor = contentResolver.query(uriWithId, null, null, null, null)

        if (cursor != null) {
            val user = MappingHelper.mapCursorToObject(cursor)
            bind(user)
            cursor.close()
        }
    }

    private fun updateFavorite() {
        btn_favorite.setImageResource(R.drawable.baseline_clear_white_48dp)
        btn_favorite.setOnClickListener {
            contentResolver.delete(uriWithId, null, null)
            loadDataSQLite(userData.id!!)
            Toast.makeText(
                this@UserDetailActivity,
                "Berhasil delete dari favorite",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun bind(userData: User) {
        Glide.with(this)
            .load(userData.avatarURL)
            .into(img_ava_detail)
        tv_name_detail.text = userData.name
        tv_username_detail.text = userData.login
        tv_info_detail.text = getString(
            R.string.info_detail,
            if (userData.company == "null") "-" else userData.company,
            if (userData.location == "null") "-" else userData.location
        )
        tv_repositories_detail.text = getString(R.string.repositories, userData.repos.toString())
    }
}