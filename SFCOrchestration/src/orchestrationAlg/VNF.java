package orchestrationAlg;

public class VNF {
	private String vnf_identity;
	private String vnf_type;
	private double cpu;
	private double memory;
	private double storage;
	
	public VNF(String identity, String type, double cpu, double memory, double storage){
		this.vnf_identity = identity;
		this.vnf_type = type;
		this.cpu = cpu;
		this.memory = memory;
		this.storage = storage;
	}

	public String getVnf_identity() {
		return vnf_identity;
	}

	public String getVnf_type() {
		return vnf_type;
	}

	public double getCpu() {
		return cpu;
	}

	public double getMemory() {
		return memory;
	}

	public double getStorage() {
		return storage;
	}
}
