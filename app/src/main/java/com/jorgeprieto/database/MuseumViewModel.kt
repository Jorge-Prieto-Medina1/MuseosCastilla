package com.jorgeprieto.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.database.data.Repo

class MuseumViewModel: ViewModel() {

    val repo = Repo()
    //función que recoge la lista de museos
    fun getMuseumData():LiveData<MutableList<Museum>>{
        val museumMutable = MutableLiveData<MutableList<Museum>>()
        repo.getMuseumData().observeForever{
            museumMutable.value = it
        }
        return museumMutable
    }

}