package orchestrationAlg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SfcRequest {
	private int headPoint;
	private List<Integer> tailPoint;
	private List<VNF> vnfList;
	private List<List<String>> routes;
	private Map<String, VNF> idAndVnf;
	
	public SfcRequest(){
		tailPoint = new ArrayList<Integer>();
		vnfList = new ArrayList<VNF>();
		routes = new ArrayList<List<String>>();
		idAndVnf = new HashMap<String, VNF>();
	}

	public int getHeadPoint() {
		return headPoint;
	}

	public List<Integer> getTailPoint() {
		return tailPoint;
	}

	public List<VNF> getVnfList() {
		return vnfList;
	}

	public List<List<String>> getRoutes() {
		return routes;
	}

	public Map<String, VNF> getIdAndVnf() {
		return idAndVnf;
	}
	
}
