package com.cszechy.backseat_driver;

import com.openxc.measurements.TransmissionGearPosition;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.TransmissionGearPosition.GearPosition;

public class MyTransmissionGearPosition implements TransmissionGearPosition.Listener {

	private TransmissionGearPosition.GearPosition position = GearPosition.NEUTRAL;
	
	@Override
	public void receive(Measurement measurement) {
		TransmissionGearPosition gearPosition = (TransmissionGearPosition) measurement;
		position = gearPosition.getValue().enumValue();
	}
	
	public TransmissionGearPosition.GearPosition getPosition() {
		return position;
	}
	
}
