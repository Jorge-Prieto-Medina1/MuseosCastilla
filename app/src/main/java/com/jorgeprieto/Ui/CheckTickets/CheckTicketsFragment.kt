package com.jorgeprieto.ui.CheckTickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jorgeprieto.museosjorgeprieto.R


class CheckTicketsFragment : Fragment() {

    private lateinit var checkTicketsModel: CheckTicketsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkTicketsModel =
            ViewModelProviders.of(this).get(CheckTicketsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_check_tickets, container, false)

        return root

    }




}