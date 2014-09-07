package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_Instructions extends Fragment {

	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if (container == null) return null;
	    view = (LinearLayout) inflater.inflate(R.layout.fragment_instructions, container, false);
	    return view;
	}
	
	public void data(String nextAction) {
		((TextView)view.findViewById(R.id.nextAction)).setText(nextAction);
	}
	
}
