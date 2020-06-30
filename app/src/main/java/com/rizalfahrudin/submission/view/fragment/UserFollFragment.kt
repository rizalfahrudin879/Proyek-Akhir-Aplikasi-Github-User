package com.rizalfahrudin.submission.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rizalfahrudin.submission.R
import com.rizalfahrudin.submission.adapter.UserAdapter
import com.rizalfahrudin.submission.viewmodel.UserFollViewModel
import kotlinx.android.synthetic.main.fragment_user_foll.*

class UserFollFragment : Fragment() {
    companion object {
        const val ARG_OBJ = "object"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var userFollViewModel: UserFollViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_foll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJ) }?.apply {
            val result: Array<String> = getStringArray(ARG_OBJ)!!
            setupViewModel(result)
            showRecyclerListViewUser()
        }
    }

    private fun showRecyclerListViewUser() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        rv_users_foll.layoutManager = LinearLayoutManager(context)
        rv_users_foll.adapter = adapter
    }

    private fun setupViewModel(result: Array<String>) {

        userFollViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserFollViewModel::class.java)

        userFollViewModel.setUserList(result)

        userFollViewModel.getUserList().observe(this.viewLifecycleOwner, Observer { items ->
            if (items != null) adapter.mUser = items
        })

    }
}