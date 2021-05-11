package dev.jorgeprieto.videoclub.utils

import android.content.Context
import android.service.media.MediaBrowserService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import java.io.InputStream
import java.nio.charset.Charset

fun Context.getJsonFromAsserts(file: String): String?{
    var json = ""
    val stream: InputStream = assets.open(file)
    val size: Int = stream.available()
    val buffer = ByteArray(size)
    stream.read(buffer)
    stream.close()

    json = String(buffer, Charset.defaultCharset())
    return json
}


fun ImageView.loadImage(image: String) {
    Glide.with(this)
        .load(image)
        .into(this)
}
    fun ViewGroup.inflate (@LayoutRes layoutRes: Int, attachRoot: Boolean = true): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachRoot)



