package com.cszechy.backseat_driver;

import com.openxc.measurements.TransmissionGearPosition;


public class ShiftingLogic {
	private CarDataPacket cardata;
	private boolean brake, accelerator, clutch;
	private boolean[] shifter;
	private int clutchThreshold, rollingSpeed, maxSuggestRPM, minSuggestRPM;
	private int prevGear;
	
	public ShiftingLogic(CarDataPacket cardata) {
		this.cardata = cardata;
		this.brake = false;
		this.shifter = new boolean[7];
		this.shifter[0] = false;
		this.shifter[1] = false;
		this.shifter[2] = false;
		this.shifter[3] = false;
		this.shifter[4] = false;
		this.shifter[5] = false;
		this.shifter[6] = false;
		this.clutch = false;
		this.accelerator = false;
		this.clutchThreshold = 25;
		this.maxSuggestRPM = 2100;
		this.minSuggestRPM = 600;
		this.rollingSpeed = 5;
	}
	
	public void setClutchThreshold(int threshold) {
		this.clutchThreshold = threshold;
	}
	
	public void setMaxSuggestRPM(int rpm) {
		this.maxSuggestRPM = rpm;
	}
	
	public void setMinSuggestRPM(int rpm) {
		this.minSuggestRPM = rpm;
	}
	
	public void setRollingSpeed(int speed) {
		this.rollingSpeed = speed;
	}
	
	private void clutch(int val) {
		this.clutch = (int)this.cardata.getClutchPedalPos()==val;
	}
	
	private void accelerator(boolean val) {
		if (this.cardata.getAccelPedalPos()!=0)
			this.accelerator = val;
		else
			this.accelerator = !val;
	}
	
	private void shifter(int val) {
		this.shifter[0] = val==0;
		this.shifter[1] = val==1;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.FIRST;
		this.shifter[2] = val==2;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.SECOND;
		this.shifter[3] = val==3;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.THIRD;
		this.shifter[4] = val==4;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.FOURTH;
		this.shifter[5] = val==5;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.FIFTH;
		this.shifter[6] = val==6;// && this.cardata.getGear()==TransmissionGearPosition.GearPosition.SIXTH;
	}
	
	private void brake(boolean val) {
		this.brake = this.cardata.getBrake()==val;
	}
	
	private int convertEnum(TransmissionGearPosition.GearPosition gearPos) {
		if (gearPos==TransmissionGearPosition.GearPosition.FIRST)
			return 1;
		else if (gearPos==TransmissionGearPosition.GearPosition.SECOND)
			return 2;
		else if (gearPos==TransmissionGearPosition.GearPosition.THIRD)
			return 3;
		else if (gearPos==TransmissionGearPosition.GearPosition.FOURTH)
			return 4;
		else if (gearPos==TransmissionGearPosition.GearPosition.FIFTH)
			return 5;
		else if (gearPos==TransmissionGearPosition.GearPosition.SIXTH)
			return 6;
		else
			return 0;
	}
	
	public void determineAction() {
		//If engine is running
		if (this.cardata.getRPM()>10){
			//If parking brake applied
			if (this.cardata.getParkStatus()) {
				//parkbrake off
			} else {	//if parking brake not applied
				//if vehicle is moving
				if (this.cardata.getMPH()>=this.rollingSpeed) {
					//if it's time to upshift
					if (this.cardata.getRPM()>=this.maxSuggestRPM) {
						//if clutch is pressed in
						if (this.cardata.getClutchPedalPos()>this.clutchThreshold) {
							//if user has upshifted already
							if (convertEnum(this.cardata.getGear())>this.prevGear) {
								clutch(this.clutchThreshold);
								accelerator(false);
							} else {	//if user has not upshifted already
								clutch(100);
								accelerator(false);
								shifter(this.prevGear+1);
							}							
						} else {	//if clutch is not pressed in 
							clutch(0);
							accelerator(true);
						}
					//if close to stalling
					} else if (this.cardata.getRPM()<=this.minSuggestRPM) {
						//if clutch is pressed in
						if (this.cardata.getClutchPedalPos()>this.clutchThreshold) {
							//if user has downshifted already
							if (convertEnum(this.cardata.getGear())<this.prevGear) {
								clutch(this.clutchThreshold);
								accelerator(true);
							} else {	//if user has not downshifted already
								clutch(100);
								accelerator(false);
								shifter(this.prevGear-1);
							}							
						} else {	//if clutch is not pressed in 
							clutch(0);
							accelerator(true);
						}
					} else { //if inbetween high and low RPM ranges
						prevGear = convertEnum(this.cardata.getGear());
					}
				} else { //If vehicle is not moving
						//if vehicle is in first
					if (this.cardata.getGear()==TransmissionGearPosition.GearPosition.FIRST) {
						//If clutch is pushed in, tell the driver to do this
						if (this.cardata.getClutchPedalPos()>this.clutchThreshold) {
							clutch(this.clutchThreshold);
							brake(false);
							accelerator(false);
							shifter(1);
							//if clutch is not in, instruct driver to do this
						} else {
							clutch(0);
							brake(false);
							accelerator(true);
							shifter(1);
						}
						//If we're not in first
					} else {
						clutch(100);
						shifter(1);
						accelerator(false);
					}
				}
			}
		} else { //if engine is not running, begin running procedures
			brake(true);
			clutch(100);
			shifter(0);
			accelerator(false);
			//turn the key
		}
	}
	
	public boolean[] getShifter() { return this.shifter; }
	public boolean getClutch() { return this.clutch; }
	public boolean getAccelerator() { return this.accelerator; }
	public boolean getBrake() { return this.brake; }
}
