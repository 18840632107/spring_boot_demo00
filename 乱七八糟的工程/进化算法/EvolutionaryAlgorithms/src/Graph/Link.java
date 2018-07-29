package Graph;

import org.json.JSONException;
import org.json.JSONObject;

public class Link {
	int leftID;
	int rightID;
	double rate;
	double bandWidth;
	double latency; 
	Boolean energyState;
	public Link(int left,int right,double bandwidth,double Latency) {
		// TODO Auto-generated constructor stub
		setLeftID(left);
		setRightID(right);
		setRate(0);
		setBandWidth(bandwidth);
		setLatency(Latency);
		setEnergyState(false);
	}
	
	public int getLeftID() {
		return leftID;
	}
	public void setLeftID(int leftID) {
		this.leftID = leftID;
	}

	public int getRightID() {
		return rightID;
	}

	public void setRightID(int rightID) {
		this.rightID = rightID;
	}

	public int getBandWidth() {
		return (int)bandWidth;
	}

	public void setBandWidth(double bandWidth) {
		this.bandWidth = bandWidth;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getLatency() {
		return latency;
	}

	public void setLatency(double latency) {
		this.latency = latency;
	}

	public Boolean getEnergyState() {
		return energyState;
	}

	public void setEnergyState(Boolean energyState) {
		this.energyState = energyState;
	}
	
	public JSONObject getLink(){
		try {
			String str="{\"leftID\":\""+leftID+"\",\"rightID\":\""+rightID+"\",\"bandWidth\":\""+bandWidth+"\",\"rate\":\""+rate+"\",\"latency\":\""+latency+"\",\"energyState\":\""+energyState+"\"}";
			return new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
}