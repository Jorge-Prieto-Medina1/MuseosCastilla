package com.jorgeprieto.database

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jorgeprieto.adapter.MusseumViewHolder
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.utils.inflate


//Clase adaptador del museo la cual se encarga de rellenar el recyclerview de museos
class MuseumAdapter (private  val listener: (Museum) -> Unit) : RecyclerView.Adapter<MusseumViewHolder> (){
    private var museumList = mutableListOf<Museum>()

    fun setListData(data:MutableList<Museum>){
        museumList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusseumViewHolder {
        val view = parent.inflate(R.layout.itemmuseum, false)
        return MusseumViewHolder(view)
    }

    override fun getItemCount(): Int = museumList.size


    override fun onBindViewHolder(holder: MusseumViewHolder, position: Int) {
        val museum = museumList[position]
        holder.bind(museum)
        holder.itemView.setOnClickListener {listener (museum)}
    }

}