package com.cszechy.backseat_driver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
	
	public void accel(boolean accel) {
		ImageView accelImage = (ImageView)view.findViewById(R.id.accelerator);
		if(accel) 
			accelImage.setImageResource(R.drawable.gas_activated);		
		else
			accelImage.setImageResource(R.drawable.gas);
	}
	
	public void brake(boolean brake) {
		ImageView brakeImage = (ImageView)view.findViewById(R.id.brake);
		if(brake)
			brakeImage.setImageResource(R.drawable.clutch_activated);
		else
			brakeImage.setImageResource(R.drawable.clutch);
	}
	
	public void clutch(boolean clutch) {
		ImageView clutchImage = (ImageView)view.findViewById(R.id.clutch);
		if(clutch)
			clutchImage.setImageResource(R.drawable.clutch_activated);
		else
			clutchImage.setImageResource(R.drawable.clutch);
	}
	
	public void shifter(int shift) {
		ImageView shiftImage = (ImageView)view.findViewById(R.id.shifter);
		if (shift == 0) shiftImage.setImageResource(R.drawable.gearnuetral_lit);
		if (shift == 1) shiftImage.setImageResource(R.drawable.gearfirst_lit);
		if (shift == 2) shiftImage.setImageResource(R.drawable.gearsecond_lit);
		if (shift == 3) shiftImage.setImageResource(R.drawable.gearthird_lit);
		if (shift == 4) shiftImage.setImageResource(R.drawable.gearforth_lit);
		if (shift == 5) shiftImage.setImageResource(R.drawable.gearfifth_lit);
		if (shift == 6) shiftImage.setImageResource(R.drawable.gearsixth_lit);
	}
	
}
