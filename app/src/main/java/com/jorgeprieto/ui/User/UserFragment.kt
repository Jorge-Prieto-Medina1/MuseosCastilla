package com.jorgeprieto.ui.User

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.ui.LoginActicities.LoginActivity
import com.jorgeprieto.museosjorgeprieto.ProviderType
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    val REQUEST_CODE = 200
    var photoURIUser:String = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user, container, false)

        return root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        closeSesion()
        foto()
        changeCredentials()
    }



    //función para cambiar la foto de usuario
    fun foto(){

        btnImageChange.setOnClickListener(){
            val prefs = this.activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val provider = prefs.getString("provider", null)

            if (provider.equals(ProviderType.BASIC.toString())) {
                if (this.activity!!.checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CODE)
                }

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE)
            } else{
                showAlert()
            }
        }

    }

    //on result de cambiar la foto de usuario
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            photoURIUser = data.toURI()
            imgChange.setImageBitmap(data.extras?.get("data") as Bitmap)
        }
        changePhoto()
    }

    //función que cierra la sesión actual
    fun closeSesion() {
        btnSignOut.setOnClickListener(){
            val prefs = this.activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            this.activity!!.finish()
        }
    }
    //funcion que guarda la nueva foto en la base de datos
    fun changePhoto(){
        var auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            var photoURI = Uri.parse(photoURIUser)
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(photoURI)
                .build()

            user.updateProfile(profileUpdates)

        }
        showSucced()
    }

    //función para cambiar las credenciales del usuario
    fun changeCredentials(){
        btnChange.setOnClickListener() {
            val prefs = this.activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val provider = prefs.getString("provider", null)

            if (provider.equals(ProviderType.BASIC.toString())) {
                val auth = FirebaseAuth.getInstance()
                if (txtPasswordChange.text.toString().trim() != "")
                    changeUserPassword(auth)
                    db.collection("users").document(getString(R.string.prefs_file)).set(
                        hashMapOf(
                            "username" to txtNameChange.toString(),
                            "address" to prefs.getString("email", null),
                            "provider" to ProviderType.BASIC.toString()
                        )
                    )
                    showSucced()
                if (txtNameChange.text.toString().trim() != "")
                    changeUserName(auth)
                    showSucced()


            }else {
                showAlert()
            }
        }
    }

    //función llamada para cambiar el nombre del usuario
    private fun changeUserName(auth: FirebaseAuth) {
        auth.currentUser?.let { user ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(txtNameChange.text.toString().trim())
                .build()
            user.updateProfile(profileUpdates)
        }
    }


    //función llamada para cambiar la contraseña
    private fun changeUserPassword(auth: FirebaseAuth) {
        auth.currentUser.updatePassword(txtPasswordChange.text.toString().trim())
    }

    //alertam mostrada cuando se intenta cambiar los datos con una cuenta de google
    private fun showAlert(){
        val builder = AlertDialog.Builder(this.activity!!)
        builder.setTitle("Error")
        builder.setMessage("Error, you can´t change your account info using a google account")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //aviso lanzado cunado los cambios se realizan con exito
    private fun showSucced(){
        val builder = AlertDialog.Builder(this.activity!!)
        builder.setTitle("Succed")
        builder.setMessage("Credentials changed restart the app to see the changes")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}