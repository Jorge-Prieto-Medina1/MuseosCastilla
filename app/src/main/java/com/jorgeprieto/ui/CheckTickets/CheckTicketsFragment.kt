package com.jorgeprieto.ui.CheckTickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.jorgeprieto.museosjorgeprieto.R
import kotlinx.android.synthetic.main.fragment_check_tickets.*


class CheckTicketsFragment : Fragment() {


    //fragmento con el lector de qr para comprobar los tickets
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

    //funcion del escaner
    private fun initScanner(){
        btnScanner.setOnClickListener(){
            val inter = IntentIntegrator(this.activity!!)
            inter.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            inter.setPrompt("Scan the ticket")
            inter.setBeepEnabled(true)
            inter.initiateScan()
            //debido a que esto es un fragment el on activity result esta en la clase navigationDrawer pues no encontre
            //forma de sobrescribirlo aqui
        }

    }

}