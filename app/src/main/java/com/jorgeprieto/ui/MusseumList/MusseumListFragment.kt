package com.jorgeprieto.ui.MusseumList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jorgeprieto.museosjorgeprieto.R


class MusseumListFragment : Fragment() {

    private lateinit var musseunListViewModel : MusseunListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        musseunListViewModel =
            ViewModelProviders.of(this).get(MusseunListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_musseum_list, container, false)

        return root
    }
}