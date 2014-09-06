package com.cszechy.backseat_driver;

import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.Measurement;

public class MyVehicleSpeed implements VehicleSpeed.Listener {

	private double speed = 0;
	
	@Override
	public void receive(Measurement measurement) {
		VehicleSpeed vehicleSpeed = (VehicleSpeed)measurement;
		speed = vehicleSpeed.getValue().doubleValue();
	}
	
	public double getSpeed() {
		return speed;
	}
	
}
