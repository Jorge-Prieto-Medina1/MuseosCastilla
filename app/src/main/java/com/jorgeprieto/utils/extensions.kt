package com.jorgeprieto.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide



fun ImageView.loadImage(image: String) {
    Glide.with(this)
        .load(image)
        .into(this)
}
    fun ViewGroup.inflate (@LayoutRes layoutRes: Int, attachRoot: Boolean = true): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachRoot)



