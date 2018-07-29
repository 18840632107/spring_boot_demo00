package Graph;

import org.json.JSONException;
import org.json.JSONObject;

public class Server extends Node {
	//核心数
	int coreNum;
	//处理能力
	double capacity;
	//内存
	int memory;
	public Server(String name,int ID,int num,double cap,int mem) {
		// TODO Auto-generated constructor stub
		nodeName=name;
		nodeID=ID;
		coreNum=num;
		capacity=cap;
		memory=mem;
	}
	
	@Override
	public JSONObject getNode() {
		// TODO Auto-generated method stub
		try {
			String str="{\"nodeID\":\""+nodeID+"\",\"nodeName\":\""+nodeName+"\",\"coreNum\":\""+coreNum+"\",\"capacity\":\""+capacity+"\",\"memory\":\""+memory+"\"}";
			return new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public int getCoreNum() {
		// TODO Auto-generated method stub
		return coreNum;
	}

	@Override
	public double getCapacity() {
		// TODO Auto-generated method stub
		return capacity;
	}

	@Override
	public int getMemory() {
		// TODO Auto-generated method stub
		return memory;
	}

	@Override
	public void setCoreNum(int CoreNum) {
		// TODO Auto-generated method stub
		coreNum=CoreNum;
	}

	@Override
	public void setCapacity(double Capacity) {
		// TODO Auto-generated method stub
		capacity=Capacity;
	}

	@Override
	public void setMemory(int Memory) {
		// TODO Auto-generated method stub
		memory=Memory;
	}

	
	//
	@Override
	public int getFlowtableNum() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Boolean getEnergyState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getControllerFlag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFlowtableNum(int FlowtableNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnergyState(Boolean EnergyState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setControllerFlag(Boolean ControllerFlag) {
		// TODO Auto-generated method stub
		
	}

}
