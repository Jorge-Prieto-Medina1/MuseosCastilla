package com.jorgeprieto.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.database.firebaseDatabase
import com.jorgeprieto.museosjorgeprieto.ProviderType
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
            if (txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmail.text.toString(),txtPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToMain(txtEmail.text.toString(), ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, wrong email or password")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goToMain(email: String, provider: ProviderType){
        val main = Intent (this@LoginActivity, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra( "provider", provider.name)
        }
        startActivity(main)
        finish()
    }
}
