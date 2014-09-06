package com.cszechy.backseat_driver;

import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.Measurement;

public class MyAcceleratorPedalPosition implements AcceleratorPedalPosition.Listener {

	private double position = 0;
	
	@Override
	public void receive(Measurement measurement) {
		// TODO Auto-generated method stub
		AcceleratorPedalPosition myPosition = (AcceleratorPedalPosition)measurement;
		position = myPosition.getValue().doubleValue();
	}
	
	public double getPosition() {
		return position;
	}

}
