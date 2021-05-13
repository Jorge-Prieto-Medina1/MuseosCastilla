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
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.jorgeprieto.Ui.LoginActivity
import com.jorgeprieto.museosjorgeprieto.ProviderType
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.ui.home.UserViewModel
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    val REQUEST_CODE = 200
    var photoURIUser:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        return root


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        closeSesion()
        foto()
        changeCredentials()
    }



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            photoURIUser = data.toURI()
            imgChange.setImageBitmap(data.extras?.get("data") as Bitmap)

        }
        changePhoto()
    }


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


    fun changeCredentials(){
        btnChange.setOnClickListener() {
            val prefs = this.activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val provider = prefs.getString("provider", null)

            if (provider.equals(ProviderType.BASIC.toString())) {
                val auth = FirebaseAuth.getInstance()
                if (txtPasswordChange.text.toString().trim() != "")
                    changeUserPassword(auth)
                    showSucced()
                if (txtNameChange.text.toString().trim() != "")
                    changeUserName(auth)
                    showSucced()
            }else {
                showAlert()
            }
        }
    }

    private fun changeUserName(auth: FirebaseAuth) {
        auth.currentUser?.let { user ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(txtNameChange.text.toString().trim())
                .build()
            user.updateProfile(profileUpdates)
        }
    }

    private fun changeUserPassword(auth: FirebaseAuth) {
        auth.currentUser.updatePassword(txtPasswordChange.text.toString().trim())
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this.activity!!)
        builder.setTitle("Error")
        builder.setMessage("Error, you can´t change your account info using a google account")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showSucced(){
        val builder = AlertDialog.Builder(this.activity!!)
        builder.setTitle("Succed")
        builder.setMessage("Credentials changed restart the app to see the changes")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}