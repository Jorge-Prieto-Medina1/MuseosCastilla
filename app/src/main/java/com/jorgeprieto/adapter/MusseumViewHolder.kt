package com.jorgeprieto.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jorgeprieto.database.Museum
import com.jorgeprieto.utils.loadImage
import kotlinx.android.synthetic.main.itemmuseum.view.*

class MusseumViewHolder (view: View) :RecyclerView.ViewHolder(view){

    //función que carga los datos en cada view holder del recyclerview
    fun bind (musseum: Museum){
        var name = musseum.nombre
        var city = musseum.localidad

        itemView.imgMusseumImage.loadImage(musseum.imagen)
        itemView.txtMusseumName.text = ("$name \n City $city")
    }

}