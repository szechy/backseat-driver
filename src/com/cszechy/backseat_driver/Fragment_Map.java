package com.cszechy.backseat_driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	    else myLocation = new LatLng(42,-83);
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0f));
	}
	
	public void calcRoute(View view) {
		mMap.setMyLocationEnabled(true);
		TextView loc = (TextView)getActivity().findViewById(R.id.loc);
		double[] data = getLatLng(loc.getText().toString());
		LatLng location = new LatLng(data[0],data[1]);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
	}
	
	private double[] getLatLng(String term) {
		try {
			URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=" + term.replaceAll(" ", "%20"));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			double[] data = new double[2];
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("\"lat\""))
					data[0] = Double.parseDouble(inputLine.substring(inputLine.indexOf(":"),inputLine.trim().length()-1).trim());
				if (inputLine.contains("\"lng\"")) {
					data[1] = Double.parseDouble(inputLine.substring(inputLine.indexOf(":"),inputLine.trim().length()-1).trim());
					break;
				}
			}
	        in.close();
	        return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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