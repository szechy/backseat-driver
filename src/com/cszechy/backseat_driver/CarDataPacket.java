package com.cszechy.backseat_driver;

import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.TransmissionGearPosition;
import com.openxc.measurements.TurnSignalStatus;

public class CarDataPacket {
	private double accelPedalPos = 0;
	private boolean clutchPedalPos = false;
	private boolean brake = false;
	private double RPM = 0;
	private IgnitionStatus.IgnitionPosition ignition = IgnitionStatus.IgnitionPosition.IGN_OFF;
	private boolean parkStatus = false;
	private TransmissionGearPosition.GearPosition gearPos = TransmissionGearPosition.GearPosition.NEUTRAL;
	private TurnSignalStatus.TurnSignalPosition turnSignal = TurnSignalStatus.TurnSignalPosition.OFF;
	private double mph = 0;
	
	public CarDataPacket(double accelPedalPos, boolean clutchPedalPos, BrakePedalStatus.BrakePosition brake, double RPM, IgnitionStatus.IgnitionPosition ignition,
			boolean parkStatus, TransmissionGearPosition.GearPosition gearPos, 
			TurnSignalStatus.TurnSignalPosition turnSignal, double mph) {
		this.accelPedalPos = accelPedalPos;
		this.clutchPedalPos = clutchPedalPos;
		this.brake = (brake!= BrakePedalStatus.BrakePosition.IDLE);
		this.RPM = RPM;
		this.ignition = ignition;
		this.parkStatus = parkStatus;
		this.gearPos = gearPos;
		this.turnSignal = turnSignal;
		this.mph = mph;
	}

	public double getAccelPedalPos() {
		return accelPedalPos;
	}
	
	public boolean getClutchPedalPos() {
		return clutchPedalPos;
	}
	
	public boolean getBrake() {
		return brake;
	}
	
	public double getRPM() {
		return RPM;
	}
	
	public IgnitionStatus.IgnitionPosition getIgnition() {
		return ignition;
	}
	
	public boolean getParkStatus() {
		return parkStatus;
	}
	
	public TransmissionGearPosition.GearPosition getGear() {
		return gearPos;
	}
	
	public TurnSignalStatus.TurnSignalPosition getTurnSignal() {
		return turnSignal;
	}
	
	public double getMPH() {
		return mph;
	}
	
	public void setAccelPedalPos(double accelPedalPosOutside) {
		accelPedalPos = accelPedalPosOutside;
	}
}
