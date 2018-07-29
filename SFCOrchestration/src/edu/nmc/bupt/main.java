package edu.nmc.bupt;

public class main {
	public static final String FILENAME = "E:\\Java\\JavaWorkSpace\\SFCOrchestration\\src\\edu\\nmc\\bupt\\topo.json";
	public static TransPhyData tpd = new TransPhyData();
	public static void main(String[] args) {
		tpd.readSubFromJson(FILENAME);
		
		SfcRequest sfcRequest = new SfcRequest(3);
		sfcRequest.getShortestPathOrchestrationResult();
	}
}