package com.jorgeprieto.ui.CheckTickets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.zxing.integration.android.IntentIntegrator
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.fragment_check_tickets.*


class CheckTicketsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_check_tickets, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initScanner()
    }

    private fun initScanner(){
        btnScanner.setOnClickListener(){
            val inter = IntentIntegrator(this.activity)
            inter.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            inter.setPrompt("Scan the ticket")
            inter.setBeepEnabled(true)
            inter.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if (result.contents== null){
                    Toast.makeText(this.activity, "canceled", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.activity, "Entrada escaneada: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }


        super.onActivityResult(requestCode, resultCode, data)
    }


}