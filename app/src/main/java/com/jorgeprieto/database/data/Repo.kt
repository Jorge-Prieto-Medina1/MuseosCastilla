package com.jorgeprieto.database.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.database.Museum

class Repo {

    fun getMuseumData(): LiveData<MutableList<Museum>> {
        val mutableData = MutableLiveData<MutableList<Museum>>()
        val db = FirebaseFirestore.getInstance()

        db.collection("museos").get().addOnSuccessListener { result ->
            val listData = mutableListOf<Museum>()
            for (document in result) {
                val imagen = document.getString("imagen")
                val nombre = document.getString("nombre")
                val id = document.getString("id")
                val latitud = document.getDouble("latitud")
                val longitud = document.getDouble("longitud")
                val localidad = document.getString("localidad")
                val puntuacion = document.getDouble("puntuacion")
                val votos = document.getLong("votos")
                val museum = Museum(id!!, imagen!!, latitud!!, localidad!!, longitud!!, nombre!!, puntuacion!!, votos!!)
                listData.add(museum)
            }
            mutableData.value = listData
        }
        return mutableData
    }
}




