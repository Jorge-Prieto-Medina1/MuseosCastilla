package com.jorgeprieto.Ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.provider.DocumentsProvider
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.ktx.Firebase
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        var email = bundle?.getString("email")
        var provider = bundle?.getString("provider")

        setPhoto()
        setup(email?: "", provider?: "")


        //guardar datos

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        closeSesion()
    }

    fun closeSesion(){
        button.setOnClickListener(){


            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            val Login = Intent (this@MainActivity, LoginActivity::class.java)
            startActivity(Login)
            finish()
        }
    }


    fun setPhoto() {

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)  == PackageManager.PERMISSION_GRANTED) {
                imageView.setImageURI(FirebaseAuth.getInstance().currentUser.photoUrl)
            }else{
                Toast.makeText (this, "Camera permission necesary", Toast.LENGTH_SHORT).show()
            }

    }

    private fun setup(email: String, provider: String){
        title = "?????"
        var emailRec = email
        var providerRec = provider
        textView.text = email
        textView2.text = provider


    }
}