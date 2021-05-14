package com.jorgeprieto.ui.MusseumList

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.database.Museum
import com.jorgeprieto.database.data.Calification
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.utils.loadImage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_register.*
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class MusseumDetail : AppCompatActivity() {

    private var musseum: Museum? = null
    private  val code = 300
    private val db = FirebaseFirestore.getInstance()
    private var uriList = ArrayList<Uri>()
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        retrieveMovie()
        renderUI()
        getStars()
        rating()
        addPhoto()
        loadPhoto()
        changePhoto()
        generateTicket()

    }

    private fun retrieveMovie() {
        musseum = intent.getSerializableExtra("musseum") as Museum?
    }

    private fun renderUI() {
        txtNameMusseumDetail.text = musseum?.nombre
        musseum?.imagen?.let { mImage ->
            imgMusseumDetail.loadImage(mImage)
        }
    }

    private fun generateTicket(){
        btnBuyTickets.setOnClickListener(){
            
        }
    }

    private fun changePhoto(){
        imgMusseumSwitch.setOnClickListener{
            if (uriList.size > 1){
                counter++
                if(counter == uriList.size){
                    counter==0
                    imgMusseumSwitch.setImageURI(this.uriList[counter])
                }else{
                    imgMusseumSwitch.setImageURI(this.uriList[counter])
                }
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(this.uriList[counter].toString())
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("No images")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }
    }

    private fun loadPhoto() {
        val museumId = (musseum!!.id)
        db.collection("imagenes").get().addOnSuccessListener { result ->
            for (document in result) {
                var imagen = document.getString("imagen")
                var idMuseo = document.getString("idMuseo")
                if (idMuseo.equals(museumId)) {
                    var URI: Uri = Uri.parse(imagen)
                    uriList.add(URI)
                }

            }
        }
    }

    private fun addPhoto(){
        btnAddPhoto.setOnClickListener{
            if (checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), code)
            }

            if (checkSelfPermission(android.Manifest.permission.CAMERA)  == PackageManager.PERMISSION_GRANTED) {
                foto()
            }else{
                Toast.makeText (this, "Camera permission necesary", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun foto(){

        if (checkSelfPermission(android.Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), code)
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == code && data != null){
            var idRam = Random().nextInt((1000000))+1
            var email = FirebaseAuth.getInstance().currentUser.email
            var id = ("$idRam$email")
            var image = data.toURI().toString()
            val museumId = (musseum!!.id)
            db.collection("imagenes").document(id).set(
                hashMapOf(
                    "imagen" to image,
                    "emailUsu" to email,
                    "idMuseo" to museumId,
                )
            )
        }
    }


    private fun rating(){
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val num = rating*2
            updateStars(num.toDouble())
        }



    }

    private fun getStars() {
        this.txtRating.text = "Rating: 0 Votes 0"
        val museumId = (musseum!!.id)
        var puntuacionTotal = 0.0
        var amount = 0
        db.collection("puntuacion").get().addOnSuccessListener { result ->
            for (document in result) {
                var puntuacion = document.getDouble("puntuacion")
                var idMuseo = document.getString("idMuseo")
                if (idMuseo.equals(museumId)){
                    amount++
                    puntuacionTotal = puntuacionTotal + puntuacion!!
                }
            }
            puntuacionTotal = puntuacionTotal/amount
            val strPunt = String.format("%.2f",puntuacionTotal)
            this.txtRating.text = "Rating: $strPunt Votes $amount"

        }

    }

    private fun updateStars(num: Double) {
        var email = FirebaseAuth.getInstance().currentUser.email
        val museumId = (musseum!!.id)
        val id = ("$museumId$email")
        db.collection("puntuacion").document(id).set(
            hashMapOf(
                "id" to id,
                "emailUsu" to email,
                "idMuseo" to museumId,
                "puntuacion" to num
            )
        )
    }

}