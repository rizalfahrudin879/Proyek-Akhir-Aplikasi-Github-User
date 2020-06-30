package  com.rizalfahrudin.favoriteapp.adapter

import User
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizalfahrudin.favoriteapp.R
import kotlinx.android.synthetic.main.item.view.*

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    var mUser = ArrayList<User>()
        set(mUser) {
            if (mUser.size > 0) this.mUser.clear()

            this.mUser.addAll(mUser)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ListViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) =
        holder.bind(mUser[position])

    override fun getItemCount(): Int = mUser.size


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                tv_name.text = user.login
                Glide.with(context)
                    .load(user.avatarURL)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_ava)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}