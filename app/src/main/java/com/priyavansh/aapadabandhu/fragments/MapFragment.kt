package com.priyavansh.aapadabandhu.fragments

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.priyavansh.aapadabandhu.R
import com.priyavansh.aapadabandhu.databinding.FragmentMapBinding

class MapFragment : Fragment() , OnMapReadyCallback {

    private var map: GoogleMap? = null
    private lateinit var binding: FragmentMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)

        // Initializing the SupportMapFragment using childFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Set up RadioGroup for map type selection
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.normal_button -> {
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
                R.id.satellite_button -> {
                    map?.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                R.id.hybrid_button -> {
                    map?.mapType = GoogleMap.MAP_TYPE_HYBRID
                }
                R.id.terrain_button -> {
                    map?.mapType = GoogleMap.MAP_TYPE_TERRAIN
                }
            }
        }
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL

        val latLong = LatLng(28.7041, 77.1025)
        val latLong2 = LatLng(28.7041, 77.1060)
        val latLong3 = LatLng(28.7041, 77.1100)

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong,15f))

        map?.uiSettings?.isZoomControlsEnabled = true
        map?.uiSettings?.isZoomGesturesEnabled = true
        map?.uiSettings?.isCompassEnabled = true
        map?.uiSettings?.isRotateGesturesEnabled = true
        map?.uiSettings?.isTiltGesturesEnabled = true
        map?.uiSettings?.isScrollGesturesEnabled = true
        map?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = true
        map?.uiSettings?.isMyLocationButtonEnabled = true

        val markerOptions = MarkerOptions()
        markerOptions.position(latLong)
        markerOptions.title("Location")
        markerOptions.snippet("This is my location")
        markerOptions.alpha(1f) // for opacity of marker
        markerOptions.draggable(true)
        map?.addMarker(markerOptions)

        val markerOptions2 = MarkerOptions()
        markerOptions2.position(latLong2)
        markerOptions2.title("Location2")
        markerOptions2.snippet("This is my location2")
        markerOptions2.alpha(1f) // for opacity of marker
        markerOptions2.draggable(true)
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        map?.addMarker(markerOptions2)
    }
}