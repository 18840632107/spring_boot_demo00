package edu.nmc.bupt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class SfcRequest implements Cloneable {
	public static TransPhyData tpd = main.tpd;
	private int phyNodeNum = tpd.getPhyNodes().size(); 	   // 物理网络节点数
	private int sfcType; 				   // SFC请求的类型，目前只有三类
	private int[] endPoints; 		   // 确定起始与目的节点
	private int vnfNum; 				   // VNF的节点数
	private int vnfNumMax = 4;    //VNF的节点数上限
	private int vnfNumMin = 2;    //VNF的节点数下限
	private int vnfResNeedMax = 5;    //VNF节点请求的资源
	private int[] deployRes; 		  // 部署结果（物理节点序列）
	private int lenDeployRes;       // 存储部署结果的数组长度
	private double delay = 0.0; 	  // 时延（跳数）
	
	//三个构造方法，用以确定SFC的类型与VNF数目
	public SfcRequest(){
		sfcType = new Random().nextInt(3)+1;
		endPoints = endPointsGeneration(tpd.getEndPoints());
		vnfNum= new Random().nextInt(vnfNumMax)%(vnfNumMax - vnfNumMin +1)+vnfNumMin;
		switch (sfcType) {
		case 1:
			deployRes = new int[vnfNum +2];
			lenDeployRes = deployRes.length;
			break;
		case 2:
			deployRes = new int[(vnfNum-1)*2+1 +3];
			lenDeployRes = deployRes.length;
			break;
		case 3:
			deployRes = new int[(vnfNum-1)*2+1 +2];
			lenDeployRes = deployRes.length;
			break;
		default:
			break;
		}
	}
	public SfcRequest(int sfcType){
		this.sfcType = sfcType;
		endPoints = endPointsGeneration(tpd.getEndPoints());
		vnfNum= new Random().nextInt(vnfNumMax)%(vnfNumMax - vnfNumMin +1)+vnfNumMin;
		switch (this.sfcType) {
		case 1:
			deployRes = new int[vnfNum +2];
			lenDeployRes = deployRes.length;
			break;
		case 2:
			deployRes = new int[(vnfNum-1)*2+1 +3];
			lenDeployRes = deployRes.length;
			break;
		case 3:
			deployRes = new int[(vnfNum-1)*2+1 +2];
			lenDeployRes = deployRes.length;
			break;
		default:
			break;
		}
	}
	public SfcRequest(int sfcType, int vnfNum){
		this.sfcType = sfcType;
		this.endPoints = endPointsGeneration(tpd.getEndPoints());
		this.vnfNum= vnfNum;
		switch (this.sfcType) {
		case 1:
			deployRes = new int[vnfNum +2];
			lenDeployRes = deployRes.length;
			break;
		case 2:
			deployRes = new int[(vnfNum-1)*2+1 +3];
			lenDeployRes = deployRes.length;
			break;
		case 3:
			deployRes = new int[(vnfNum-1)*2+1 +2];
			lenDeployRes = deployRes.length;
			break;
		default:
			break;
		}
	}
	// 产生随机的终端节点数组(不同网络的用户终端节点不同)
	public int[] endPointsGeneration(int[] clientNode) {
		Vector<Integer> endPoints = new Vector<Integer>();
		for (int i = 1; i < clientNode.length; i++) {
			endPoints.add(clientNode[i]);
		}
		int[] res = new int[3];
		for (int i = 0; i < 3; i++) {
			Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
			int index = r.nextInt(endPoints.size());
			res[i] = endPoints.get(index).intValue();
			endPoints.remove(index);
		}
		return res;
	}
	// 产生最短路径解
	public void shortestPathGeneration() {
		deployRes[0] = endPoints[0]; // 起点
		deployRes[lenDeployRes - 1] = endPoints[1]; // 终点（之一）
		if (sfcType == 2) {
			deployRes[lenDeployRes - 2] = endPoints[2];
		}
		
		int VNFnumCount = 0;
		int[] tmpPathArray1 = null;
		int[] tmpPathArray2 = null;
		List<Integer> filterRepeat = new ArrayList<>();
		switch (sfcType) {
		case 1:
			VNFnumCount = 0;
			tmpPathArray1 = Dijkstra.dijkstraPathArray(tpd.getAvjDelay(), deployRes[0], deployRes[lenDeployRes-1]);
			for (int i = 0; i < tmpPathArray1.length; i++) {
				int nodeCost = new Random().nextInt(vnfResNeedMax);
				if (VNFnumCount < vnfNum && nodeCost < tpd.getPhyNodes().get(tmpPathArray1[i]).getCpu()) {
					deployRes[i+1] = tmpPathArray1[i];
					VNFnumCount++;
				}
				if (VNFnumCount >= vnfNum) {
					break;
				}
			}
			break;
		case 2:
			//第一条路
			VNFnumCount = 0;			
			tmpPathArray1 = Dijkstra.dijkstraPathArray(tpd.getAvjDelay(), deployRes[0], deployRes[lenDeployRes-1]);
			for (int i = 0; i < tmpPathArray1.length; i++) {
				int nodeCost = new Random().nextInt(vnfResNeedMax);
				if (VNFnumCount < vnfNum && nodeCost < tpd.getPhyNodes().get(tmpPathArray1[i]).getCpu()) {
					deployRes[++VNFnumCount] = tmpPathArray1[i];
					filterRepeat.add(tmpPathArray1[i]);
				}
				if (VNFnumCount >= vnfNum) {
					break;
				}
			}
			//第二条路
			VNFnumCount = 0;
			tmpPathArray2 = Dijkstra.dijkstraPathArray(tpd.getAvjDelay(), deployRes[1], deployRes[lenDeployRes-2]);
			for (int i = 1; i < tmpPathArray2.length; i++) {
				int nodeCost = new Random().nextInt(vnfResNeedMax);
				if (VNFnumCount < vnfNum && nodeCost < tpd.getPhyNodes().get(tmpPathArray2[i]).getCpu() && !filterRepeat.contains(tmpPathArray2[i])) {
					VNFnumCount++;
					deployRes[VNFnumCount+ vnfNum] = tmpPathArray2[i];
				}
				if (VNFnumCount >= (vnfNum-1)) {
					break;
				}
			}
			break;
		case 3:
			filterRepeat.clear();
			//第一条路
			VNFnumCount = 0;			
			tmpPathArray1 = Dijkstra.dijkstraPathArray(tpd.getAvjDelay(), deployRes[0], deployRes[lenDeployRes-1]);
			for (int i = 0; i < tmpPathArray1.length; i++) {
				int nodeCost = new Random().nextInt(vnfResNeedMax);
				if (VNFnumCount < vnfNum && nodeCost < tpd.getPhyNodes().get(tmpPathArray1[i]).getCpu()) {
					deployRes[++VNFnumCount] = tmpPathArray1[i];
					filterRepeat.add(tmpPathArray1[i]);
				}
				if (VNFnumCount >= vnfNum) {
					break;
				}
			}
			//第二条路
			VNFnumCount = 0;
			tmpPathArray2 = Dijkstra.dijkstraPathArray(tpd.getAvjDelay(), deployRes[1], deployRes[lenDeployRes-1]);
			for (int i = 1; i < tmpPathArray2.length; i++) {
				int nodeCost = new Random().nextInt(vnfResNeedMax);
				if (VNFnumCount < vnfNum && nodeCost < tpd.getPhyNodes().get(tmpPathArray2[i]).getCpu() && !filterRepeat.contains(tmpPathArray2[i])) {
					VNFnumCount++;
					deployRes[VNFnumCount+ vnfNum] = tmpPathArray2[i];
				}
				if (VNFnumCount >= (vnfNum-1)) {
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	// 得到最短路径解的部署结果
	public void getShortestPathOrchestrationResult() {
		shortestPathGeneration();
		
		System.out.print("部署的SFC类型为：");
		switch (sfcType) {
		case 1:
			System.out.println("Linear");
			System.out.println("SFC请求起始节点为："+deployRes[0]+"目的节点为："+deployRes[lenDeployRes-1]);
			System.out.println("映射结果为：");
			for (int i = 1; i < deployRes.length-1; i++) {
				System.out.println("VNF"+i+"映射到物理节点"+deployRes[i] + " ");
			}
			break;
		case 2:
			System.out.println("Branch");
			System.out.println("SFC请求路径1:");
			System.out.println("起始节点为："+deployRes[0]+"目的节点为："+deployRes[lenDeployRes-1]);
			System.out.println("映射结果为：");
			for (int i = 1; i < vnfNum+1; i++) {
				System.out.println("VNF"+i+"映射到物理节点"+deployRes[i] + " ");
			}
			System.out.println();
			System.out.println("SFC请求路径2:");
			System.out.println("起始节点为："+deployRes[0]+"目的节点为："+deployRes[lenDeployRes-2]);
			System.out.println("映射结果为：");
			System.out.println("VNF1"+"映射到物理节点"+deployRes[1] + " ");
			for (int i = vnfNum+1; i < lenDeployRes-2; i++) {
				System.out.println("VNF"+i+"映射到物理节点"+deployRes[i] + " ");
			}
			break;
		case 3:
			System.out.println("Loop");
			System.out.println("SFC请求路径1:");
			System.out.println("起始节点为："+deployRes[0]+"目的节点为："+deployRes[lenDeployRes-1]);
			System.out.println("映射结果为：");
			for (int i = 1; i < vnfNum+1; i++) {
				System.out.println("VNF"+i+"映射到物理节点"+deployRes[i] + " ");
			}
			System.out.println();
			System.out.println("SFC请求路径2:");
			System.out.println("起始节点为："+deployRes[0]+"目的节点为："+deployRes[lenDeployRes-1]);
			System.out.println("映射结果为：");
			System.out.println("VNF1"+"映射到物理节点"+deployRes[1] + " ");
			for (int i = vnfNum+1; i <= lenDeployRes-2; i++) {
				System.out.println("VNF"+i+"映射到物理节点"+deployRes[i] + " ");
			}		
			break;
		default:
			break;
		};		
	}
	
	public int getPhyNodeNum() {
		return phyNodeNum;
	}
	public void setPhyNodeNum(int phyNodeNum) {
		this.phyNodeNum = phyNodeNum;
	}
	public int getSfcType() {
		return sfcType;
	}
	public void setSfcType(int sfcType) {
		this.sfcType = sfcType;
	}
	public int[] getEndPoints() {
		return endPoints;
	}
	public void setEndPoints(int[] endPoints) {
		this.endPoints = endPoints;
	}
	public int getVnfNum() {
		return vnfNum;
	}
	public void setVnfNum(int vnfNum) {
		this.vnfNum = vnfNum;
	}
	public int[] getDeployRes() {
		return deployRes;
	}
	public void setDeployRes(int[] deployRes) {
		this.deployRes = deployRes;
	}
	public double getDelay() {
		return delay;
	}
	public void setDelay(double delay) {
		this.delay = delay;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		SfcRequest sfcRequest = (SfcRequest) super.clone();
		sfcRequest.vnfNum = this.vnfNum;
		sfcRequest.deployRes = this.deployRes.clone();
		sfcRequest.delay = this.delay;
		return sfcRequest;
	}
}
