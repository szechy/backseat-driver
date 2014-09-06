package com.cszechy.backseat_driver;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class Fragment_Map extends Fragment {
	
	private static View view;	
	private static GoogleMap mMap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if (container == null) return null;
	    view = (LinearLayout) inflater.inflate(R.layout.fragment_map, container, false);
        setUpMapIfNeeded();
	    return view;
	}
	
	public void setUpMapIfNeeded() {
	    if (mMap == null) {
	        mMap = ((SupportMapFragment) Home.fm.findFragmentById(R.id.location_map)).getMap();
	        if (mMap != null) setUpMap();
	    }
	}
	
	private void setUpMap() {
	    mMap.setMyLocationEnabled(true);
	    LatLng myLocation = null;
	    Location location = mMap.getMyLocation();
	    if (location != null) myLocation = new LatLng(location.getLatitude(),location.getLongitude());
	    else myLocation = new LatLng(43,26);
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0f));
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    if (mMap != null) setUpMap();
	    if (mMap == null) {
	        mMap = ((SupportMapFragment) Home.fm.findFragmentById(R.id.location_map)).getMap();
	        if (mMap != null) setUpMap();
	    }
	}
	
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (mMap != null) {
	        Home.fm.beginTransaction().remove(Home.fm.findFragmentById(R.id.location_map)).commit();
	        mMap = null;
	    }
	}
	
}