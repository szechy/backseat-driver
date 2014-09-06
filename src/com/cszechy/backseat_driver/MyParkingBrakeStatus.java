package com.cszechy.backseat_driver;

import com.openxc.measurements.ParkingBrakeStatus;
import com.openxc.measurements.Measurement;

public class MyParkingBrakeStatus implements ParkingBrakeStatus.Listener {

	private boolean parkingBrakeOn = true;
	
	@Override
	public void receive(Measurement measurement) {
		ParkingBrakeStatus status = (ParkingBrakeStatus)measurement;
		parkingBrakeOn = status.getValue().booleanValue();
	}
	
	public boolean getParkingBrakeOn() {
		return parkingBrakeOn;
	}

}
