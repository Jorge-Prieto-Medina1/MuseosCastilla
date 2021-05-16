package com.jorgeprieto.ui.Map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.model.Model
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.jorgeprieto.database.Museum
import com.jorgeprieto.database.data.Repo
import com.jorgeprieto.museosjorgeprieto.R
import com.jorgeprieto.ui.MusseumList.MusseumDetail
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {



    val repo = Repo()
    private lateinit var map:GoogleMap
    private var mapReady = false
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync {
                googleMap -> map = googleMap
                mapReady = true
                onMapReady(googleMap)
        }

        return rootView
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        repo.getMuseumData().observeForever{
            for (museum in it){
                val posicion = LatLng(museum.latitud, museum.longitud)
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(posicion)
                        .title(museum.nombre)
                )
                marker.tag = museum.id
                map.setOnMarkerClickListener(this)
            }
        }

        map.moveCamera( CameraUpdateFactory.newLatLngZoom( LatLng(38.80323832848393, -4.053181179593821) , 9.0f) )


    }


    override fun onMarkerClick(marker: Marker): Boolean {
        val intent = Intent(this.activity!!, MusseumDetail::class.java)
        db.collection("museos").document(marker.tag.toString()).get().addOnSuccessListener { result ->
            val imagen = result.getString("imagen")
            val nombre = result.getString("nombre")
            val id = result.getString("id")
            val latitud = result.getDouble("latitud")
            val longitud = result.getDouble("longitud")
            val localidad = result.getString("localidad")
            val museum = Museum(id!!, imagen!!, latitud!!, localidad!!, longitud!!, nombre!!)
            intent.putExtra("musseum", museum)
            startActivity(intent)

        }
        return false
    }

}



