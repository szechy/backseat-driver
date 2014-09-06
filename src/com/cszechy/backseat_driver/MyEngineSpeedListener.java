package com.cszechy.backseat_driver;

import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.Measurement;

public class MyEngineSpeedListener implements EngineSpeed.Listener{

	private double rpm = 0;
	
	@Override
	public void receive(Measurement measurement) {
		//Cast to EngineSpeed
		EngineSpeed speed = (EngineSpeed)measurement;
		//Rip out actual speed from object for accessing
		rpm = speed.getValue().doubleValue();
	}
	
	public double getRPM() {
		return rpm;
	}

}
