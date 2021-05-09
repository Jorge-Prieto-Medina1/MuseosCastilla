package com.jorgeprieto.Ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_pantalla_splash.*
import kotlinx.android.synthetic.main.activity_register.*


class PantallaSplash : AppCompatActivity() {
    val REQUEST_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_splash)
        getPerm()
        //animaciones
        val animationImage = AnimationUtils.loadAnimation(this, R.anim.image_animation)
        val superiorTxt = AnimationUtils.loadAnimation(this, R.anim.superior_txt_animation)
        val inferiorTxt = AnimationUtils.loadAnimation(this, R.anim.inferior_txt_animation)


        //elementos
        var image = this.imgSplash
        var superior = this.txtCreador1
        var inferior = this.txtCreador2

        //asignacion de elementos
        image.animation = animationImage
        superior.animation = superiorTxt
        inferior.animation = inferiorTxt

        //inicio de las animaciones
        image.animation.start()
        superior.animation.start()
        inferior.animation.start()

        val splashScrenTime = 4000
        val Main = Intent (this@PantallaSplash, LoginActivity::class.java)

        Handler().postDelayed({
            startActivity(Main)
            finish()
        }, splashScrenTime.toLong())
    }

    fun getPerm() {

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }

        if (checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
        }
    }
}