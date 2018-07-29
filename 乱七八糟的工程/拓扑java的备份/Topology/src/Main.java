import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		int K = 6;             //number of pods
		int coreLayer = (K / 2)*(K / 2);
		int aggrLayer = (K / 2)*K;
		int edgeLayer = (K / 2)*K;
		
		List<SWITCH> coreSwitchArray = new ArrayList<SWITCH>();
		List<SWITCH> aggrSwitchArray = new ArrayList<SWITCH>();
		List<SWITCH> edgeSwitchArray = new ArrayList<SWITCH>();
		
		int swNum=0;
		// add core ovs
		for (int i = 0; i < coreLayer; i++){
			SWITCH core = new SWITCH();
			core.setSwitchId(swNum);
			core.setInDegree (K);
			core.setOutDegree(K);
			core.setASid(swNum++);
			coreSwitchArray.add(core);
		}
		// add aggregation ovs and add edge ovs
		for (int i = 0; i < aggrLayer; i++){
			SWITCH aggr = new SWITCH();
			aggr.setSwitchId(swNum);
			aggr.setInDegree (K / 2);
			aggr.setOutDegree(K / 2);
			aggr.setASid(swNum++);
			aggrSwitchArray.add(aggr);
		}
		for (int i = 0; i < edgeLayer; i++){
			SWITCH edge = new SWITCH();
			edge.setSwitchId(swNum);
			edge.setInDegree (K / 2);
			edge.setOutDegree(K / 2);
			edge.setASid(swNum++);
			edgeSwitchArray.add(edge);
		}
		
		List<LINK> linkArray = new ArrayList<LINK>();
		int edgeNum=0;
		// add links between core and aggregation ovs
		for(int i=0;i<coreLayer;i++){
			for(int j=(i/(K/2));j<aggrLayer;j+=(K/2)){
				LINK link =new LINK();
				link.setEdgeId(edgeNum++);
				
				double r = Math.random();
				link.setLength(r);
				r = Math.random();
				Random random = new Random();
				int ii = random.nextInt(3);
				link.setDelay(r+ii);
				
				/*
				 add the agreement of link
				 */
				linkArray.add(link);
			}
		}
		// add links between aggregation and edge ovs
		for(int i=0;i<aggrLayer;i+=(K/2)){
			for(int j=i;j<(i+(K/2));j++){
				for(int k=i;k<(i+(K/2));k++){
					LINK link =new LINK();
					link.setEdgeId(edgeNum++);
					
					double r = Math.random();
					link.setLength(r);
					r = Math.random();
					Random random = new Random();
					int ii = random.nextInt(3);
					link.setDelay(r+ii);
					
					/*
					 add the agreement of link
					 */
					linkArray.add(link);
				}
			}
		}
		// add hosts and its links with edge ovs
		List<HOST> hostArray = new ArrayList<HOST>();
		int hostNum=0;
		for(int i=0;i<edgeLayer;i++){
			for(int j=0;j<K/2;j++){
				LINK link =new LINK();
				link.setEdgeId(edgeNum++);
				
				double r = Math.random();
				link.setLength(r);
				r = Math.random();
				Random random = new Random();
				int ii = random.nextInt(3);
				link.setDelay(r+ii);
				
				/*
				 add the agreement of link
				 */
				linkArray.add(link);
				
				HOST host = new HOST();
				host.setHostId(hostNum++);
				hostArray.add(host);
			}
		} 
			
		// generate the topology file
		try {  
			  
	        String Symbol = "Topology: ( "+ K +" Pods, "+ edgeNum + " Edges )"
	        		+ "\r\nModel (3 - ASWaxman):  1000 1000 100 1  2  0.15000000596046448 0.20000000298023224 1 1 10.0 1024.0 "
	        		+ "\r\n";  
	        File file = new File("F:\\Musubi\\topo.txt");  
	        if (!file.exists()) {  
	            file.createNewFile();  
	        }  
	        FileWriter fw = new FileWriter(file);  
	        BufferedWriter bw = new BufferedWriter(fw);  
	        bw.write(Symbol);  
	        bw.write("Node:"+" £¨ "+K+" £© "+"\r\n"); 	
	        for(int i=0;i<coreLayer;i++){
	        	String node = null;
	        	node=String.format("%-7d %-7d %-7d %-7d %-7d %-7d %-7s", 
	        			           coreSwitchArray.get(i).getSwitchId(),
	        			           coreSwitchArray.get(i).getXpos(),
	        			           coreSwitchArray.get(i).getYpos(),
	        			           coreSwitchArray.get(i).getInDegree(),
	        			           coreSwitchArray.get(i).getOutDegree(),
	        			           coreSwitchArray.get(i).getASid(),
	        			           "AS_NODE" ); 
	        	bw.write(node+"\r\n");
	        }
	        for(int i=0;i<aggrLayer;i++){
	        	String node = null;
	        	node=String.format("%-7d %-7d %-7d %-7d %-7d %-7d %-7s", 
	        					   aggrSwitchArray.get(i).getSwitchId(),
	        					   aggrSwitchArray.get(i).getXpos(),
	        					   aggrSwitchArray.get(i).getYpos(),
	        					   aggrSwitchArray.get(i).getInDegree(),
	        					   aggrSwitchArray.get(i).getOutDegree(),
	        					   aggrSwitchArray.get(i).getASid(),
	        			           "AS_NODE" ); 
	        	bw.write(node+"\r\n");
	        }
	        for(int i=0;i<edgeLayer;i++){
	        	String node = null;
	        	node=String.format("%-7d %-7d %-7d %-7d %-7d %-7d %-7s", 
	        			           edgeSwitchArray.get(i).getSwitchId(),
	        			           edgeSwitchArray.get(i).getXpos(),
	        			           edgeSwitchArray.get(i).getYpos(),
	        			           edgeSwitchArray.get(i).getInDegree(),
	        			           edgeSwitchArray.get(i).getOutDegree(),
	        			           edgeSwitchArray.get(i).getASid(),
	        			           "AS_NODE" ); 
	        	bw.write(node+"\r\n");
	        }
	        
	        bw.write("\r\n\r\n"+"Edges:"+" £¨ "+edgeNum+" £© "+"\r\n");
	        
	        for(int i=0;i<edgeNum;i++){
	        	String link0 = null;
	        	link0=String.format("%-7d %-7d %-7d %-23.17f %-23.16f %-7.1f "+"%-7d %-7d %-7s", 
	        			           linkArray.get(i).getEdgeId(),
	        			           linkArray.get(i).from,
	        			           linkArray.get(i).to,
	        			           linkArray.get(i).getLength()*1000,
	        			           linkArray.get(i).getDelay(),
	        			           linkArray.get(i).getBandwidth(),
	        			           linkArray.get(i).ASfrom,
	        			           linkArray.get(i).ASto,
	        			           "E_AS	U"); 
	       		bw.write(link0+"\r\n");
	        }
	        bw.flush();  
	        bw.close();  

	    } 
		catch (IOException e) {  
	        e.printStackTrace(); 
	    }  
		
    }
} 
class SWITCH{
	public int switchId = 0;
	public int xpos = 0;
	public int ypos = 0;
	public int inDegree = 0;
	public int outDegree = 0;
	public int ASid = 0;
	public String type = "AS_NODE";
	public int getSwitchId() {
		return switchId;
	}
	public void setSwitchId(int switchId) {
		this.switchId = switchId;
	}
	public int getXpos() {
		return xpos;
	}
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
	public int getInDegree() {
		return inDegree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public int getOutDegree() {
		return outDegree;
	}
	public void setOutDegree(int outDegree) {
		this.outDegree = outDegree;
	}
	public int getASid() {
		return ASid;
	}
	public void setASid(int aSid) {
		ASid = aSid;
	}
	
};

class LINK{
	public int edgeId = 0;
	public int from = 0;
	public int to = 0;
	public double length = 0.0;
	public double delay = 0.0;
	public double bandwidth = 10.0;
	public int ASfrom = 0;
	public int ASto = 0;
	public String type = "E_AS";
	public String unknow = "U";
	public int getEdgeId() {
		return edgeId;
	}
	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getDelay() {
		return delay;
	}
	public void setDelay(double delay) {
		this.delay = delay;
	}
	public double getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}
	
};

class HOST{
	public int hostId = 0;

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	
};

