package com.raflisalam.storyapp.ui.maps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raflisalam.storyapp.R
import com.raflisalam.storyapp.pref.UserSession
import com.raflisalam.storyapp.viewmodel.get.maps.MapsViewModel
import com.raflisalam.storyapp.viewmodel.get.maps.MapsViewModelFactory
import com.raflisalam.storyapp.viewmodel.session.SessionFactoryViewModel
import com.raflisalam.storyapp.viewmodel.session.SessionViewModel

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory.getInstance(requireContext())
    }

    private lateinit var userSession: UserSession
    private lateinit var session: SessionViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.listMaps.observe(viewLifecycleOwner) { response ->
            if (response.isNotEmpty()) {
                response.forEach { data ->
                    if (data.lat != null && data.lon != null) {
                        val coordinate = LatLng(data.lat, data.lon)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(coordinate)
                                .title(data.name)
                                .snippet(data.description)
                        )
                    }
                }
                val latestStories = response[0]
                val default = LatLng(latestStories.lat ?: 0.0, latestStories.lon ?: 0.0)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(default, 5f))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupViewModel()
    }

    private fun setupViewModel() {
        userSession = UserSession.newInstance(requireContext().dataStore)
        session = ViewModelProvider(this, SessionFactoryViewModel(userSession))[SessionViewModel::class.java]

        userSession.userToken.asLiveData().observe(viewLifecycleOwner) { token ->
            token?.let {
                viewModel.getStoriesMap(token)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
    }
}