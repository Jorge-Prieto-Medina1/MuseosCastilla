package com.jorgeprieto.Ui

import android.app.Activity
import android.app.Instrumentation
import androidx.activity.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.database.firebaseDatabase
import com.jorgeprieto.museosjorgeprieto.ProviderType
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.security.Permission
import java.security.Provider
import java.util.jar.Manifest

class RegisterActivity : AppCompatActivity() {

    var fotoBol: Boolean = false
    val REQUEST_CODE = 200
    var permission = arrayOf(CAMERA_SERVICE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register()
        back()
        capturePhoto()
    }

    fun capturePhoto() {
        btnImage.setOnClickListener {

            if (checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED) {
                foto()
            }else{
                Toast.makeText (this, "Camera permission necesary", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
            }

        }
    }


    fun foto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            var foto = data.extras?.get("data") as Bitmap
            imgRg.setImageBitmap(foto)
            fotoBol = true
        }
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
            if (txtEmailRg.text.isNotEmpty() && txtPasswordRg.text.isNotEmpty() && txtName.text.isNotEmpty() && fotoBol){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmailRg.text.toString(), txtPasswordRg.text.toString()).addOnCompleteListener(){
                    if (it.isSuccessful){
                        backHome(txtEmailRg.text.toString(),ProviderType.BASIC)
                        login()
                    }else{
                        showAlert()
                    }
                }

            }

        }
    }

    private fun login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmailRg.text.toString(),txtPasswordRg.text.toString()).addOnCompleteListener {
            if (it.isSuccessful) {

            } else {
                showAlert()
            }
        }
    }

    private fun addRestOfData(){
        var auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            val username = txtName.text.toString()

        }

        val username = txtName.text.toString()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, operation failed")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun backHome(email: String, provider: ProviderType){
        val main = Intent (this@RegisterActivity, MainActivity::class.java)
        startActivity(main)
        finish()
    }

}