package com.cszechy.backseat_driver;

import android.util.Log;

import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.TransmissionGearPosition;


public class ShiftingLogic {
	private CarDataPacket cardata;
	private boolean brake, accelerator, clutch;
	private int shifter;
	private int clutchThreshold, rollingSpeed, maxSuggestRPM, minSuggestRPM;
	private int prevGear;
	private IgnitionStatus.IgnitionPosition key = IgnitionStatus.IgnitionPosition.IGN_OFF;
	private String nextAction;
	private boolean shiftingUp = false, shiftingDown = false;
	
	public ShiftingLogic(CarDataPacket cardata, int prevGear, boolean shiftingUp,
			boolean shiftingDown) {
		this.cardata = cardata;
		this.brake = false;
		this.shifter = prevGear;
		this.prevGear = prevGear;
		this.clutch = false;
		this.accelerator = false;
		this.clutchThreshold = 25;
		this.maxSuggestRPM = 1300;
		this.minSuggestRPM = 800;
		this.rollingSpeed = 8;
		this.nextAction = "";
		this.key = cardata.getIgnition();
		this.shiftingUp = shiftingUp;
		this.shiftingDown = shiftingDown;
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
	
	private void clutch(boolean val) {
		this.clutch = val;
	}
	
	private void accelerator(boolean val) {
		/*if (this.cardata.getAccelPedalPos()!=0)
			this.accelerator = val;
		else
			this.accelerator = !val;*/
		this.accelerator = val;
	}
	
	private void shifter(int val) {
		shifter = val;
	}
	
	private void brake(boolean val) {
		//this.brake = this.cardata.getBrake()==val;
		this.brake = val;
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
		if (key == IgnitionStatus.IgnitionPosition.IGN_RUN){
			//If parking brake applied
			if (this.cardata.getParkStatus()) {
				clutch(true);
				accelerator(false);
				brake(true);
				shifter(1);
				this.nextAction="Release the parking brake!";
			}
			//if parking brake not applied
			else {	
				//if vehicle is moving
				if (this.cardata.getMPH()>=this.rollingSpeed) {
					//if it's time to upshift
					if ((this.cardata.getRPM()>=this.maxSuggestRPM) & (shifter < 7)) {
						//if clutch is pressed in
						if (this.cardata.getClutchPedalPos()) {
							//tell user to shift up
							if(!shiftingUp)
								shifter(shifter+1);
							this.nextAction = ("Move into the next gear and release the clutch");
							clutch(true);
							accelerator(false);
							brake(false);
							shiftingUp = true;
						}	//if just finished pushing in clutch, finish shifting up
						/*else if (shiftingUp) {
							shiftingUp = false;
							clutch(false);
							accelerator(false);
							brake(false);
							this.nextAction = ("Begin accelerating");
						}		*/
						//if clutch is not pressed in
						else {
							//if(!shiftingUp) {
							//	shifter(shifter+1);
							//}
							shiftingUp = false;
							clutch(true);
							accelerator(false);
							brake(false);
							this.nextAction="Push the clutch in to begin upshifting";
						}
					//if close to stalling
					} else if (this.cardata.getRPM()<=this.minSuggestRPM) {
						//if clutch is pressed in
						if (this.cardata.getClutchPedalPos()) {
							//if user has downshifted already
							//if (shiftingDown) {
								clutch(false);
								accelerator(true);
								brake(false);
								shiftingDown = true;
								this.nextAction="Move the shifter down, then release the clutch";
							}					
						//if clutch is not pressed in
						else {	 
							if(!shiftingDown)
								shifter(shifter-1);
							shiftingDown = false;
							clutch(true);
							brake(false);
							accelerator(true);
							this.nextAction = "Press in the clutch to begin downshifting";
						}
					}
					//if inbetween high and low RPM ranges
					else {
						clutch(false);
						accelerator(true);
						brake(false);
						shiftingDown = false;
						shiftingUp = false;
						//this.nextAction = "Maintain current speed";
					}
				}	
				//if vehicle is stationary 
				else { 
					//If clutch is pushed in
					if (this.cardata.getClutchPedalPos()) {
						clutch(false);
						brake(false);
						accelerator(true);
						shifter(1);
						this.nextAction = "Shift into first, and begin to accelerate " +
								"while coming off the clutch";
					} 
					//clutch not in, brake is
					else if (brake) {
						clutch(true);
						brake(false);
						accelerator(false);
						shifter(0);
						this.nextAction = "Press in the clutch.";
					}
					//clutch and brake are not pushed in 
					else if (!brake){	
						clutch(true);
						brake(true);
						accelerator(false);
						shifter(0);
						this.nextAction = "Press in the clutch and the brake.";
					}
				}
			}
		} else { //if engine is not running, START THE FUCKING CAR
			brake(true);
			clutch(true);
			shifter(0);
			accelerator(false);
			this.nextAction = "Please turn on the car, and place your feet on the clutch and brake.";
		}
	}
	
	public String getNextDirection() { 
		return this.nextAction; 
		}
	public int getShifter() {
		return shifter;
	}
	public boolean getClutch() {
		return this.clutch; 
	}
	public boolean getAccelerator() { 
		return this.accelerator; 
	}
	public boolean getBrake() { 
		return this.brake; 
	}
	
	public boolean getShiftUp() {
		return shiftingUp;
	}
	
	public boolean getShiftDown() {
		return shiftingDown;
	}
	
	public double getSpeed() {
		return cardata.getMPH();
	}
	
	public void printParams() {
		//Log.d("brake", "" + cardata.getBrake());
		//Log.d("acceleration pedal", "" + cardata.getAccelPedalPos());
		Log.d("clutch", "" + cardata.getClutchPedalPos());
		Log.d("engine speed", "" + cardata.getRPM());
		Log.d("nextAction", "" + nextAction);
		Log.d("road speed", "" + cardata.getMPH());
		//Log.d("parking brake", "" + cardata.getParkStatus());
		//Log.d("ignition", "" + key.toString());
		Log.d("shifter", "" + shifter);
	}
}
