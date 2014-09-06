package com.cszechy.backseat_driver;

import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.IgnitionStatus.IgnitionPosition;
import com.openxc.measurements.Measurement;

public class MyIgnitionStatus implements IgnitionStatus.Listener {
	//enum states are OFF, ACCESSORY, START, RUN
	private IgnitionPosition status = IgnitionPosition.OFF;
	
	@Override
	public void receive(Measurement measurement) {
		IgnitionStatus igniteStatus = (IgnitionStatus) measurement;
		status = igniteStatus.getValue().enumValue();
	}
	
	public IgnitionPosition getStatus() {
		return status;
	}

}
