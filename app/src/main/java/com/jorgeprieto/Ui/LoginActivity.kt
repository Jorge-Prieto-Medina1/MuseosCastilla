package com.jorgeprieto.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register()
        login ()
    }


    private fun register (){

        btnRegisterLg.setOnClickListener{
            val register = Intent (this@LoginActivity, RegisterActivity::class.java)
                startActivity(register)
                finish()
        }
    }

    private fun login (){

        btnLogin.setOnClickListener{
            if (txtEmailRg.text.isNotEmpty() && txtPasswordRg.text.isNotEmpty() && txtName.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmailRg.text.toString(),
                    txtPasswordRg.text.toString()).addOnCompleteListener {

                }
            }
        }

    }
}
