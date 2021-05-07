package com.jorgeprieto.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.security.Provider

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register()
        back()
    }


    private fun back (){

        btnBackRg.setOnClickListener{
            val login = Intent (this@RegisterActivity, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }


    private fun register (){

        btnRegisterRg.setOnClickListener{
            if (txtEmailRg.text.isNotEmpty() && txtPasswordRg.text.isNotEmpty() && txtName.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmailRg.text.toString(),
                    txtPasswordRg.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        backHome(it.result?.user?.email?: "", ProviderType.BASIC)
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
        builder.setMessage("Error al realizar el registro")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun backHome(email: String, provider: ProviderType){
        val login = Intent (this@RegisterActivity, LoginActivity::class.java).apply {
            putExtra("email", email)
            putExtra( "provider", provider.name)
        }
        startActivity(login)
        finish()
    }

}