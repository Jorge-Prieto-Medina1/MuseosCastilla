package com.jorgeprieto.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.jorgeprieto.museosjorgeprieto.ProviderType
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.log

object firebaseDatabase {



    fun registerBase(email: String, password:String): Boolean{
        var register = false

        return register
    }


    fun loginBase(email: String, password:String): Boolean{
        var login = false

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener() {
            login = it.isSuccessful
        }

        return login

    }

}