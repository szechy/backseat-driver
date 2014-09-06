package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fragment_Instructions extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if (container == null) return null;
	    View view = (LinearLayout) inflater.inflate(R.layout.fragment_instructions, container, false);
	    return view;
	}
	
}
