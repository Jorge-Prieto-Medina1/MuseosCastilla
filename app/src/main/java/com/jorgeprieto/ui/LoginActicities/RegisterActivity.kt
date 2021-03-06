package com.jorgeprieto.ui.LoginActicities

import android.app.Activity
import androidx.activity.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.NavigationDrawerMuseoActivity
import com.jorgeprieto.museosjorgeprieto.ProviderType
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    // boolean que recoge si se ha realizado la foto, codigo, uri de la foto e instancia de la base de datos
    var fotoBol: Boolean = false
    val REQUEST_CODE = 200
    var photoURIUser:String = ""
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register()
        back()
        capturePhoto()
    }

    //función que comprueba los permisos para sacar fotos, si se tienen te manda a hacer la foto, si no te pide
    //los permisos
    fun capturePhoto() {
        btnImage.setOnClickListener {

            if (checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
            }

            if (checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED) {
                foto()
            }else{
                Toast.makeText (this, "Camera permission necesary", Toast.LENGTH_SHORT).show()
            }

        }
    }


    //función para realizar la foto
    fun foto(){

        if (checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    //el codigo que se ejecuta despues de realizar la foto, guarda la uri de la imagen,
    //pone la imagen en el image view y vuelve verdadero el booleano de la foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            photoURIUser = data.toURI()
            imgRg.setImageBitmap(data.extras?.get("data") as Bitmap)
            fotoBol = true
        }
    }


    //funcion que cierra la pantalla de registro y vueleve a abrir la de login
    private fun back (){

        btnBackRg.setOnClickListener{
            val login = Intent (this@RegisterActivity, LoginActivity::class.java)
            startActivity(login)
            finish()
        }
    }

    //función que comprueba si los campos están completos y registra al usuario
    private fun register (){
        btnRegisterRg.setOnClickListener{
            if (txtEmailRg.text.isNotEmpty() && txtPasswordRg.text.isNotEmpty() && txtName.text.isNotEmpty() && fotoBol){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmailRg.text.toString(), txtPasswordRg.text.toString()).addOnCompleteListener(){
                    if (it.isSuccessful){
                        login()
                    }else{
                        showAlert()
                    }
                }

            }

        }
    }

    //función que realiza el login despues de registrarte
    private fun login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(txtEmailRg.text.toString(),txtPasswordRg.text.toString()).addOnCompleteListener {
            if (it.isSuccessful) {
                addRestOfData()
                goMain(txtEmailRg.text.toString(),ProviderType.BASIC)
            } else {
                showAlert()
            }
        }
    }

    //función que añade la foto y el nombre de usuario al usuario recién registrado
    private fun addRestOfData(){
        var auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            val username = txtName.text.toString()
            var photoURI = Uri.parse(photoURIUser)
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoURI)
                .build()

            user.updateProfile(profileUpdates)

            db.collection("users").document(getString(R.string.prefs_file)).set(
                hashMapOf(
                    "username" to username,
                    "address" to txtEmailRg.toString(),
                    "provider" to ProviderType.BASIC.toString()
                )
            )

        }
        goMain( txtEmailRg.toString(), ProviderType.BASIC)
    }


    //alerta si falla el registro
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error, operation failed")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //función que manda al usuario a la main activity junto con el email y el provider
    private fun goMain(email: String, provider: ProviderType){
        val main = Intent (this@RegisterActivity, NavigationDrawerMuseoActivity::class.java).apply {
            putExtra("email", email)
            putExtra( "provider", provider.name)
        }
        startActivity(main)
        finish()
    }

}