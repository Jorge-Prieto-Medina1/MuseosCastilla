package com.jorgeprieto.ui.MusseumList


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.jorgeprieto.museosjorgeprieto.R
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_qr.*


class QrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        showImage()
    }

    private fun showImage(){
        val bundle = intent.extras
        var nameUser = bundle!!.getString("nameUser")
        var nameMuseum = bundle!!.getString("nameMuseum")
        val multiFormatWriter: MultiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode("The ticket was bought by $nameUser and it is for the $nameMuseum museum", BarcodeFormat.QR_CODE, 500, 500)
        val bitmap = BarcodeEncoder().createBitmap(bitMatrix)
        imgQrCode.setImageBitmap(bitmap)
    }
}