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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.jorgeprieto.database.Museum
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.utils.loadImage
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.collections.ArrayList

class MusseumDetail : AppCompatActivity() {

    private var museum: Museum? = null
    private  val code = 300
    private val db = FirebaseFirestore.getInstance()
    private var dowloadList = ArrayList<String>()
    private var counter = 0
    private var storageReference:StorageReference? = null
    private var imageUri:Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        storageReference = FirebaseStorage.getInstance().getReference()
        retrieveMovie()
        renderUI()
        getStars()
        rating()
        addPhoto()
        loadPhoto()
        changePhoto()
        generateTicket()
        getUserStars()

    }

    private fun retrieveMovie() {
        museum = intent.getSerializableExtra("musseum") as Museum?
    }

    private fun renderUI() {
        txtNameMusseumDetail.text = museum?.nombre
    }

    private fun getUserStars() {
        val museumId = (museum!!.id)
        var email = FirebaseAuth.getInstance().currentUser.email
        val id = ("$museumId$email")
        db.collection("puntuacion").document(id).get().addOnSuccessListener { result ->
            var puntuacion = result.getDouble("puntuacion")
            var idMuseo = result.getString("idMuseo")

            if (idMuseo.equals(museumId)) {
                ratingBar.setRating (puntuacion!!.toFloat()/2)
            }

        }
    }


    private fun generateTicket(){
        btnBuyTickets.setOnClickListener {
            val nameMuseum = museum!!.nombre
            val nameUser = FirebaseAuth.getInstance().currentUser.displayName
                val qr = Intent (this , QrActivity::class.java).apply {
                    putExtra("nameMuseum", nameMuseum)
                    putExtra("nameUser", nameUser.toString())
                }
                startActivity(qr)

        }
    }

    private fun changePhoto(){
        imgMuseumDetail.setOnClickListener{
            if (dowloadList.size >= 1){
                if(counter == dowloadList.size){
                    counter=0
                    imgMuseumDetail.loadImage(this.dowloadList[counter])
                    counter++
                }else{
                    imgMuseumDetail.loadImage(this.dowloadList[counter])
                    counter++
                }
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
        dowloadList = ArrayList()
        val museumId = (museum!!.id)
        dowloadList.add(museum!!.imagen)
        imgMuseumDetail.loadImage(museum!!.imagen)
        db.collection("imagenes").get().addOnSuccessListener { result ->
            for (document in result) {
                var imagen = document.getString("imagen")
                var idMuseo = document.getString("idMuseo")
                if (idMuseo.equals(museumId)) {
                    var image = storageReference!!.child("imagenes/"+imagen)
                    image.downloadUrl.addOnSuccessListener { result ->
                        dowloadList.add(result.toString())
                    }

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
            imageUri = data.data
            imgMuseumDetail.setImageBitmap(data.extras?.get("data") as Bitmap)
            uploadFile()
        }
    }

    private fun uploadFile(){
        if (imageUri != null){
            val imageRef = storageReference!!.child("imagenes/"+ UUID.randomUUID().toString())
            imageRef.putFile(imageUri!!).addOnSuccessListener {
                Toast.makeText(applicationContext,"File Uploaded", Toast.LENGTH_SHORT).show()
                var idRam = Random().nextInt((1000000))+1
                var email = FirebaseAuth.getInstance().currentUser.email
                var id = ("$idRam$email")
                var image = imageRef.name
                val museumId = (museum!!.id)
                db.collection("imagenes").document(id).set(
                    hashMapOf(
                        "imagen" to image,
                        "emailUsu" to email,
                        "idMuseo" to museumId,
                    )
                )
            }.addOnFailureListener{
                Toast.makeText(applicationContext,"Upload failed", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun rating(){
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val num = rating*2
            updateStars(num.toDouble())
        }

    }

    private fun getStars() {
        val museumId = (museum!!.id)
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
        val museumId = (museum!!.id)
        val id = ("$museumId$email")
        db.collection("puntuacion").document(id).set(
            hashMapOf(
                "id" to id,
                "emailUsu" to email,
                "idMuseo" to museumId,
                "puntuacion" to num
            )
        )
        getStars()

    }

}