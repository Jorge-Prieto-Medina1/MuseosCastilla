package dev.jorgeprieto.videoclub.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.jorgeprieto.videoclub.R
import dev.jorgeprieto.videoclub.model.Movie
import dev.jorgeprieto.videoclub.utils.loadImage
import kotlinx.android.synthetic.main.itemmovie.*

class DetailActivity : AppCompatActivity(){

    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        retrieveMovie()
        renderUI()

    }

    private  fun retrieveMovie(){
        movie = intent.getSerializableExtra("movie") as Movie?
    }

    private fun renderUI(){
        movieTitle.text = movie?.name
        movieDescription.text = movie?.descripion
        movie?.cover?.let { mCover ->
            movieCover.loadImage(mCover)
        }
    }

}