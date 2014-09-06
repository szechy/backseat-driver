package com.cszechy.backseat_driver;

import com.openxc.measurements.TurnSignalStatus;
import com.openxc.measurements.Measurement;

public class MyTurnSignalStatus implements TurnSignalStatus.Listener {

	private TurnSignalStatus.TurnSignalPosition position = TurnSignalStatus.TurnSignalPosition.OFF;
	
	@Override
	public void receive(Measurement measurement) {
		TurnSignalStatus status = (TurnSignalStatus)measurement;
		position = status.getValue().enumValue();
	}
	
	public TurnSignalStatus.TurnSignalPosition getPosition() {
		return position;
	}
}
