import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int?,
    var login: String?,
    var name: String?,
    var avatarURL: String?,
    var company: String?,
    var location: String?,
    var repos: Int? = 0
) : Parcelable
