package com.cszechy.backseat_driver;

import com.openxc.measurements.TurnSignalStatus;
import com.openxc.measurements.TransmissionGearPosition;
import com.openxc.measurements.IgnitionStatus;
import android.os.AsyncTask;

public class AdviceGeneratorTask extends AsyncTask<CarDataPacket, Void, String>{
	
	CarDataPacket current = new CarDataPacket(0, 0, IgnitionStatus.IgnitionPosition.OFF, 
			true, TransmissionGearPosition.GearPosition.NEUTRAL,
			TurnSignalStatus.TurnSignalPosition.OFF, 0);
	
	@Override
	protected String doInBackground(CarDataPacket... params) {
		// TODO Auto-generated method stub
		return null;
	}
}