package  com.rizalfahrudin.favoriteapp.helper

import User
import android.database.Cursor
import com.rizalfahrudin.favoriteapp.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NAME))
                val ava = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVA))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.COMPANY))
                val location =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOCATION))
                val repos = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.REPOS))

                userList.addAll(listOf(User(id, username, name, ava, company, location, repos)))
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): User {
        var user = User(null, null, null, null, null, null, null)

        userCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.USERNAME))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NAME))
            val ava = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVA))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOCATION))
            val repos = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.REPOS))
            user = User(id, username, name, ava, company, location, repos)
        }
        return user
    }
}