package com.jorgeprieto.ui.CheckTickets

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
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



     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null){
                if (result.contents== null){
                    txtResultQr.text =  "canceled"
                }else{
                    txtResultQr.text =  "${result.contents}"
                }
            }else{
                super.onActivityResult(requestCode, resultCode, data)
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initScanner()
    }

    private fun initScanner(){
        btnScanner.setOnClickListener(){
            val inter = IntentIntegrator(this.activity!!)
            inter.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            inter.setPrompt("Scan the ticket")
            inter.setBeepEnabled(true)
            inter.initiateScan()

        }

    }

}