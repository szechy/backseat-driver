package com.cszechy.backseat_driver;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.BrakePedalStatus;
import com.openxc.measurements.BrakePedalStatus.BrakePosition;
import com.openxc.measurements.ClutchPedalStatus;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.ParkingBrakeStatus;
import com.openxc.measurements.TransmissionGearPosition;
import com.openxc.measurements.TurnSignalStatus;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.remote.VehicleServiceException;

public class VehicleConnection implements ServiceConnection {
	
	CarDataPacket current = null;

	private boolean updateFlag = false;
	
	private VehicleManager mVehicleManager = null;
	private double accelPedalPos = 0;
	private boolean clutchPedalPos = false;
	private BrakePedalStatus.BrakePosition brake = BrakePedalStatus.BrakePosition.IDLE;
	private double rpm = 0;	//engine speed
	private double mph = 0;	//vehicle speed
	private IgnitionStatus.IgnitionPosition igniteStatus = IgnitionStatus.IgnitionPosition.IGN_OFF;
	private boolean parkStatus;
	private TransmissionGearPosition.GearPosition gearPos = TransmissionGearPosition.GearPosition.NEUTRAL;
	private TurnSignalStatus.TurnSignalPosition turnSignal = TurnSignalStatus.TurnSignalPosition.OFF;
    
    // Called when the connection with the VehicleManager service is established, i.e. bound.
    public void onServiceConnected(ComponentName className, IBinder service) {

    // When the VehicleManager starts up, we store a reference to it
    // here in "mVehicleManager" so we can call functions on it
    // elsewhere in our code
    	Log.i("HomeActivity", "VehicleManager binded");
    mVehicleManager = ((VehicleManager.VehicleBinder) service)
            .getService();

    // We want to receive updates whenever the EngineSpeed changes. We
    // have an EngineSpeed.Listener (see above, mSpeedListener) and here
    // we request that the VehicleManager call its receive() method
    // whenever the EngineSpeed changes
    try {
    	mVehicleManager.addListener(AcceleratorPedalPosition.class, mAccelPedal);
    	mVehicleManager.addListener(EngineSpeed.class, mRPM);
    	mVehicleManager.addListener(IgnitionStatus.class, mIgnition);
    	mVehicleManager.addListener(ParkingBrakeStatus.class, mParkStatus);
    	mVehicleManager.addListener(ClutchPedalStatus.class, mClutch);
    	//mVehicleManager.addListener(TransmissionGearPosition.class, mGear);
    	mVehicleManager.addListener(TurnSignalStatus.class, mSignal);
        mVehicleManager.addListener(VehicleSpeed.class, mSpeed);
    } catch (VehicleServiceException e) {
        e.printStackTrace();
    } catch (UnrecognizedMeasurementTypeException e) {
        e.printStackTrace();
    	}
    }

	@Override
	public void onServiceDisconnected(ComponentName name) {
	    Log.w("HomeActivity", "VehicleManager Service  disconnected unexpectedly");
	    mVehicleManager = null;
	}
	
	public void removeAllListeners() {
    	
    	try {
    		mVehicleManager.removeListener(AcceleratorPedalPosition.class, mAccelPedal);
        	mVehicleManager.removeListener(EngineSpeed.class, mRPM);
        	mVehicleManager.removeListener(IgnitionStatus.class, mIgnition);
        	mVehicleManager.removeListener(ParkingBrakeStatus.class, mParkStatus);
			//mVehicleManager.removeListener(TransmissionGearPosition.class, mGear);
        	mVehicleManager.removeListener(ClutchPedalStatus.class, mClutch);
			mVehicleManager.removeListener(TurnSignalStatus.class, mSignal);
	        mVehicleManager.removeListener(VehicleSpeed.class, mSpeed);
		} catch (VehicleServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	
	public void addAllListeners() {
    	
    	try {
    		mVehicleManager.addListener(AcceleratorPedalPosition.class, mAccelPedal);
        	mVehicleManager.addListener(EngineSpeed.class, mRPM);
        	mVehicleManager.addListener(IgnitionStatus.class, mIgnition);
        	mVehicleManager.addListener(ParkingBrakeStatus.class, mParkStatus);
        	//mVehicleManager.addListener(TransmissionGearPosition.class, mGear);
        	mVehicleManager.addListener(ClutchPedalStatus.class, mClutch);
			mVehicleManager.addListener(TurnSignalStatus.class, mSignal);
			mVehicleManager.addListener(VehicleSpeed.class, mSpeed);
		} catch (VehicleServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecognizedMeasurementTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public VehicleManager getVehicleManager() {
		return mVehicleManager;
	}
	
	public void setVehicleManager(VehicleManager newManager) {
		mVehicleManager = newManager;
	}
	
	public void printOutDebugSensors() {
		Log.d("AccelPedal", "position: " + accelPedalPos);
		Log.d("EngineSpeed", "RPM: " + rpm);
		Log.d("IgnitionStatus", "status: " + igniteStatus);
		Log.d("ParkingBrakeStatus", "parkStatus: " + parkStatus);
		//Log.d("TransmissionGearPosition", "gearPosition: " + gearPos);
		Log.d("TurnSignalStatus", "signalStatus: " + turnSignal);
		Log.d("VehicleSpeed", "speed: " + mph);
	}

	  private AcceleratorPedalPosition.Listener mAccelPedal = new AcceleratorPedalPosition.Listener() {
		@Override
		public void receive(Measurement measurement) {
			// TODO Auto-generated method stub
			AcceleratorPedalPosition AccelPedal = (AcceleratorPedalPosition)measurement;
			VehicleConnection.this.accelPedalPos = AccelPedal.getValue().doubleValue();
			VehicleConnection.this.updateFlag = true;
		}
	};
	
	private EngineSpeed.Listener mRPM = new EngineSpeed.Listener() {
		@Override
		public void receive(Measurement measurement) {
			//Cast to EngineSpeed
			EngineSpeed speed = (EngineSpeed)measurement;
			//Rip out actual speed from object for accessing
			VehicleConnection.this.rpm = speed.getValue().doubleValue();
			updateFlag = true;
		}
	};
	
	private IgnitionStatus.Listener mIgnition = new IgnitionStatus.Listener() {
		//enum states are OFF, ACCESSORY, START, RUN
		@Override
		public void receive(Measurement measurement) {
			IgnitionStatus igniteStatus = (IgnitionStatus) measurement;
			VehicleConnection.this.igniteStatus = igniteStatus.getValue().enumValue();
		}
	};
	
	private ParkingBrakeStatus.Listener mParkStatus = new ParkingBrakeStatus.Listener() {
		@Override
		public void receive(Measurement measurement) {
			ParkingBrakeStatus status = (ParkingBrakeStatus)measurement;
			VehicleConnection.this.parkStatus = status.getValue().booleanValue();
		}
	};
	/*private TransmissionGearPosition.Listener mGear = new TransmissionGearPosition.Listener() {
		@Override
		public void receive(Measurement measurement) {
			TransmissionGearPosition gearPosition = (TransmissionGearPosition) measurement;
			VehicleConnection.this.gearPos = gearPosition.getValue().enumValue();
		}
	};*/
	private TurnSignalStatus.Listener mSignal = new TurnSignalStatus.Listener() {
		@Override
		public void receive(Measurement measurement) {
			TurnSignalStatus status = (TurnSignalStatus)measurement;
			VehicleConnection.this.turnSignal = status.getValue().enumValue();
		}
	};
	private VehicleSpeed.Listener mSpeed = new VehicleSpeed.Listener() {	
		@Override
		public void receive(Measurement measurement) {
			VehicleSpeed vehicleSpeed = (VehicleSpeed)measurement;
			VehicleConnection.this.mph = vehicleSpeed.getValue().doubleValue();
		}
	};
	private BrakePedalStatus.Listener mBrake = new BrakePedalStatus.Listener() {	
		@Override
		public void receive(Measurement measurement) {
			BrakePedalStatus brakePedal = (BrakePedalStatus)measurement;
			VehicleConnection.this.brake = brakePedal.getValue().enumValue();
		}
	};
	
	private ClutchPedalStatus.Listener mClutch = new ClutchPedalStatus.Listener() {	
		@Override
		public void receive(Measurement measurement) {
			ClutchPedalStatus clutchPedal = (ClutchPedalStatus)measurement;
			VehicleConnection.this.clutchPedalPos = clutchPedal.getValue().booleanValue();
		}
	};
	
	public CarDataPacket getAllData() {
		return new CarDataPacket(accelPedalPos, clutchPedalPos, brake, rpm, igniteStatus, 
				parkStatus, TransmissionGearPosition.GearPosition.NEUTRAL, turnSignal, mph);
	}
}
