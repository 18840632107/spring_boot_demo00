package Graph;

import org.json.JSONException;
import org.json.JSONObject;

public class Route extends Node {
	int flowtableNum;
	Boolean energyState;
	Boolean controllerFlag;
	public Route(String name,int ID,int num) {
		// TODO Auto-generated constructor stub
		nodeName=name;
		nodeID=ID;
		flowtableNum=num;
		energyState=false;
		controllerFlag=false;
	}
	
	@Override
	public JSONObject getNode() {
		// TODO Auto-generated method stub
		try {
			String str="{\"nodeID\":\""+nodeID+"\",\"nodeName\":\""+nodeName+"\",\"flowtableNum\":\""+flowtableNum+"\",\"energyState\":\""+energyState+"\",\"controllerFlag\":\""+controllerFlag+"\"}";
			return new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	@Override
	public int getFlowtableNum() {
		// TODO Auto-generated method stub
		return flowtableNum;
	}

	@Override
	public Boolean getEnergyState() {
		// TODO Auto-generated method stub
		return energyState;
	}

	@Override
	public Boolean getControllerFlag() {
		// TODO Auto-generated method stub
		return controllerFlag;
	}

	@Override
	public void setFlowtableNum(int FlowtableNum) {
		// TODO Auto-generated method stub
		flowtableNum=FlowtableNum;
	}

	@Override
	public void setEnergyState(Boolean EnergyState) {
		// TODO Auto-generated method stub
		energyState=EnergyState;
	}

	@Override
	public void setControllerFlag(Boolean ControllerFlag) {
		// TODO Auto-generated method stub
		controllerFlag=ControllerFlag;
	}
	
	

	//server 无用
	@Override
	public int getCoreNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMemory() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public void setCoreNum(int CoreNum) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void setCapacity(double Capacity) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void setMemory(int Memory) {
		// TODO Auto-generated method stub
		
	}
	

}
