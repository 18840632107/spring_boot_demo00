package edu.nmc.bupt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

public class TransPhyData {	
	private double[][] avjBw;
	private double[][] avjDelay;
	private List<PhyNode> phyNodes;
	private int[] endPoints = {-1,1,13,14};  //初始化用户端点（-1为占位）
	private Map<Integer, List<Integer>> nodeNei;

	public void readSubFromJson(String fileName){
		nodeNei = new HashMap<Integer, List<Integer>>();
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			while((line = reader.readLine()) != null){
				try{
					JSONObject dataJson = new JSONObject(line);
					JSONArray nodes = dataJson.getJSONArray("nodes");
					JSONArray links = dataJson.getJSONArray("links");
					avjBw = new double[nodes.length()][nodes.length()];
					avjDelay = new double[nodes.length()][nodes.length()];
					phyNodes = new ArrayList<PhyNode>();
					for(int i = 0; i < nodes.length(); i++){
						for(int j = 0; j < nodes.length(); j++){
							if(i == j){
								avjBw[i][i] = 0;
								avjDelay[i][i] = 0;
							}else{
								avjBw[i][j] = -1;
								avjDelay[i][j] = 999;
							}
						}
					}
					for(int i = 0; i < nodes.length(); i++){
						JSONObject tem = nodes.getJSONObject(i);
						int id = tem.getInt("nodeId");
						double cpu = tem.getDouble("cpu");
						double memory = tem.getDouble("memory");
						double storage = tem.getDouble("storage");
//						String name = tem.getString("nodeName");
						PhyNode tnode = new PhyNode(id, cpu, memory, storage);
						phyNodes.add(tnode);
					}
					for(int i = 0; i < links.length(); i++){
						JSONObject tem = links.getJSONObject(i);
						int from = tem.getInt("from");
						int to = tem.getInt("to");
						double bw = tem.getDouble("bandwidth");
						double delay = tem.getDouble("delay");
						avjBw[from][to] = avjBw[to][from] = bw;
						avjDelay[from][to] = avjDelay[to][from] = delay;
						if(nodeNei.get(from) == null){
							List<Integer> tmp = new ArrayList<Integer>();
							tmp.add(to);
							nodeNei.put(from, tmp);
						}else
							nodeNei.get(from).add(to);
						if(nodeNei.get(to) == null){
							List<Integer> tmp = new ArrayList<Integer>();
							tmp.add(from);
							nodeNei.put(to, tmp);
						}else
							nodeNei.get(to).add(from);
					}
					for(int i = 0; i < phyNodes.size(); i++)
						System.out.print(phyNodes.get(i).getNodeId() + " ");
					System.out.println();
					for(int i = 0; i < nodes.length(); i++){
						for(int j = 0; j < nodes.length(); j++)
							System.out.print(avjBw[i][j] + " ");
						System.out.println();
					}
					System.out.println();
					for(int i = 0; i < nodes.length(); i++){
						for(int j = 0; j < nodes.length(); j++)
							System.out.print(avjDelay[i][j] + " ");
						System.out.println();
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void helper(List<List<Integer>> res, List<Integer> list, Map<Integer, List<Integer>> map, int cur, int to){
		if(cur == to){
			//list.add(to);
			res.add(new ArrayList<Integer>(list));
			return;
		}
		List<Integer> tem = map.get(cur);
		for(int i = 0; i < tem.size(); i++){
			if(list.contains(tem.get(i))) continue;
			list.add(tem.get(i));
			helper(res, list, map, tem.get(i), to);
			list.remove(list.size() - 1);
		}
	}
	
	public List<List<Integer>> getAllPath(int from, int to){
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		list.add(from);
		helper(res, list, nodeNei, from, to);
		res.sort(new Comparator<List<Integer>>(){
			public int compare(List<Integer> a, List<Integer> b){
				return Integer.valueOf(a.size()).compareTo(b.size());
			}
		});
		return res;
	}
	
	public double[][] getAvjBw() {
		return avjBw;
	}

	public double[][] getAvjDelay() {
		return avjDelay;
	}

	public List<PhyNode> getPhyNodes() {
		return phyNodes;
	}

	public int[] getEndPoints() {
		return endPoints;
	}

	public void setEndPoints(int[] endPoints) {
		this.endPoints = endPoints;
	}

}
