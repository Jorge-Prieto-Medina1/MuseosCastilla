package com.jorgeprieto.ui.MusseumList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jorgeprieto.database.Museum
import com.jorgeprieto.database.MuseumAdapter
import com.jorgeprieto.database.MuseumViewModel
import com.jorgeprieto.museosjorgeprieto.R.*
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_musseum_list.*

//fragmento del recycler de museos
class MusseumListFragment : Fragment() {



    private lateinit var adapter: MuseumAdapter
    private val viewModel by lazy { ViewModelProviders.of(this).get(MuseumViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.fragment_musseum_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MuseumAdapter(::onMuseumClicked)
        recyclerview.adapter = adapter
        observeData()

    }


    fun observeData(){
        viewModel.getMuseumData().observe(this.activity!!, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    //funcion para abrir cada uno de los museos al pulsarlos
    private fun onMuseumClicked(museum: Museum) {
        val intent = Intent(this.activity!!, MusseumDetail::class.java)
        intent.putExtra("musseum", museum)
        startActivity(intent)
    }

}