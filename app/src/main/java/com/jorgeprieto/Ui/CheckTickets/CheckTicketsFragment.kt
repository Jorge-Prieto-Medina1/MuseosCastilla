package com.jorgeprieto.ui.CheckTickets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user.view.*


class CheckTicketsFragment : Fragment() {

    private lateinit var checkTicketsModel: CheckTicketsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkTicketsModel =
            ViewModelProviders.of(this).get(CheckTicketsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        checkTicketsModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root

    }




}