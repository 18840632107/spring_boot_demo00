package Graph;

import org.json.JSONObject;

public abstract class Node {
	String nodeName;
	int nodeID;
	
	abstract public JSONObject getNode();
	
	//server
	abstract public int getCoreNum();
	abstract public double getCapacity();
	abstract public int getMemory();
	abstract public void setCoreNum(int CoreNum);
	abstract public void setCapacity(double Capacity);
	abstract public void setMemory(int Memory);
	
	//route
	abstract public int getFlowtableNum();
	abstract public Boolean getEnergyState();
	abstract public Boolean getControllerFlag();
	abstract public void setFlowtableNum(int FlowtableNum);
	abstract public void setEnergyState(Boolean EnergyState);
	abstract public void setControllerFlag(Boolean ControllerFlag);
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
