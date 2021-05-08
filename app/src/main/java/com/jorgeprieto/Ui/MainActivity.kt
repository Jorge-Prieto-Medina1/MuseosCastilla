package com.jorgeprieto.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.DocumentsProvider
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        var email = bundle?.getString("email")
        var provider = bundle?.getString("provider")

        setup(email?: "", provider?: "")
    }

    private fun setup(email: String, provider: String){
        title = "?????"
        var emailRec = email
        var providerRec = provider
        textView.text = email
        textView2.text = provider

    }
}