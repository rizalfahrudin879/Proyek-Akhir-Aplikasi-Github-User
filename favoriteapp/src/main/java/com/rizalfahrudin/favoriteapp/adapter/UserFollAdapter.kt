package  com.rizalfahrudin.favoriteapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rizalfahrudin.favoriteapp.view.fragment.UserFollFragment
import com.rizalfahrudin.favoriteapp.view.fragment.UserFollFragment.Companion.ARG_OBJ

class UserFollAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment =
            UserFollFragment()
        fragment.arguments = Bundle().apply {
            putStringArray(ARG_OBJ, arrayOf("$position", username))
        }
        return fragment
    }


}
