package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment {
	
	private static View view;	
	private static GoogleMap mMap;
	private static Double latitude, longitude;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if (container == null) return null;
	    view = (RelativeLayout) inflater.inflate(R.layout.fragment_map, container, false);
	    //latitude = 26.78;
        //longitude = 72.56;
        setUpMapIfNeeded();
	    return view;
	}
	
	public static void setUpMapIfNeeded() {
	    if (mMap == null) {
	        mMap = ((SupportMapFragment) Home.fm.findFragmentById(R.id.location_map)).getMap();
	        if (mMap != null) setUpMap();
	    }
	}
	
	private static void setUpMap() {
	    mMap.setMyLocationEnabled(true);
	    //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
	    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));
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