package dev.jorgeprieto.videoclub.adapter

import android.view.View
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import dev.jorgeprieto.videoclub.model.Movie
import dev.jorgeprieto.videoclub.utils.loadImage
import kotlinx.android.synthetic.main.itemmovie.view.*

class MoviesViewHolder (view: View) :RecyclerView.ViewHolder(view){
    fun bind (movie: Movie){
        itemView.movieTitle.text = movie.name
        itemView.movieCover.loadImage(movie.cover)
    }

}