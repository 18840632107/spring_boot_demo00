package edu.nmc.bupt;

public class PhyNode {
	private int nodeId;
//	private String nodeName;
	private double cpu;
	private double memory;
	private double storage;
	
	public PhyNode(int nodeId, double cpu, double memory, double storage){
		this.nodeId = nodeId;
//		this.nodeName = nodeName;
		this.cpu = cpu;
		this.memory = memory;
		this.storage = storage;
	}
	
	public int getNodeId() {
		return nodeId;
	}
//	public String getNodeName() {
//		return nodeName;
//	}
	public double getCpu() {
		return cpu;
	}
	public void setCpu(double cpu) {
		this.cpu = cpu;
	}
	public double getMemory() {
		return memory;
	}
	public void setMemory(double memory) {
		this.memory = memory;
	}
	public double getStorage() {
		return storage;
	}
	public void setStorage(double storage) {
		this.storage = storage;
	}	

}
