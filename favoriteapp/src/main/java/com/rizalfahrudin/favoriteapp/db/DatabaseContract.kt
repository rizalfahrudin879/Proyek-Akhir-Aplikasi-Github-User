package  com.rizalfahrudin.favoriteapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.rizalfahrudin.submission"
    const val SCHEME = "content"

    class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVA = "ava"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOS = "repos"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}