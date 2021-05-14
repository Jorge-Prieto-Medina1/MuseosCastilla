package com.jorgeprieto.database

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jorgeprieto.adapter.MusseumViewHolder
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.utils.inflate


class MuseumAdapter (private  val listener: (Museum) -> Unit) : RecyclerView.Adapter<MusseumViewHolder> (){
    private var museumList = mutableListOf<Museum>()

    fun setListData(data:MutableList<Museum>){
        museumList = data
    }

    fun refreshList(dataMusuem: MutableList<Museum>){
        this.museumList.addAll(dataMusuem)
        notifyDataSetChanged()
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