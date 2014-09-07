package com.cszechy.backseat_driver;

public class FuzzyGearLogic {
	double prevSpeed = 0;
	//int currentGear = 0;
	
	public FuzzyGearLogic(double speed) {
		prevSpeed = speed;
	}
	
	public int correctGear(double speed, int currentGear) {
		int newGear = currentGear;
		
		//change in speed over last 0.6 seconds
		double speedDiff = (speed - prevSpeed)/0.6;
		//0-60 in 12 seconds = 3mph/0.6 seconds.
		//Make that non-negotiable rate of do-not-increase-anything.
		
		//inside of that rate... time for fuzzy logic!
		if(speedDiff < 3 & speedDiff > -3) {
			if(0 < speed & 8 > speed) {
				//should probably be first gear
				if(currentGear > 3)
					newGear--;
			}
			else if(8 <= speed & 15 > speed) {
				//should probably be second gear
				if(currentGear > 4)
					newGear--;
			}
			else if(15 <= speed & 24 > speed) {
				//should probably be third gear
				if(currentGear > 5)
					newGear--;
			}
			else if(24 <= speed & 40 > speed) {
				//should probably be fourth gear
				if(currentGear == 6)
					newGear--;
			}
			else if(40 <= speed & 55 > speed) {
				//should probably be fifth gear
				if(currentGear < 3)
					newGear++;
			}
			else if (55 <= speed) {
				if(currentGear < 3)
					newGear++;
				//should probably be sixth gear
			}
		}
		prevSpeed = speed;
		//outside of that rate... meh. we're done.
		return newGear;
	}
}