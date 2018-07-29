package Graph;

import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Graph {
	
	public Node[] nodes;
	public Link[] links;
	public int[][] adjacencyMatrix;
	public int routeNum,serverNum;
	
	public Graph(String file,boolean flag) {
		// TODO Auto-generated constructor stub
		init(file,flag);
		initMatrix();
	}    

	private void init(String file,boolean flag) {
		// TODO Auto-generated method stub
		double c=0.3,b=0.3;
		if(flag){
			c=2;
			b=1;
		}
		FileReader fin = null;
		Scanner scanner = null;
		try {
			fin = new FileReader(System.getProperty("user.dir")+"\\"+file);
			scanner=new Scanner(fin);
			Random random=new Random(1);
			if(scanner.hasNext()){
				nodes=new Node[scanner.nextInt()];
				links=new Link[scanner.nextInt()];
				for (int i = 0; i < nodes.length; i++) {
					nodes[i]=new Server("s"+i,i,Data.CORE_NUM,(int)(Data.CAPACITY*(c+random.nextDouble())),Data.MEMORY);
					//System.out.println(i+" "+(int)nodes[i].getCapacity());
				}
				for (int i = 0; scanner.hasNext()&&i< links.length; i++) {
					links[i]=new Link(scanner.nextInt(),scanner.nextInt(),(int)(Data.BAND_WIDTH*(c+b*random.nextDouble())),Data.LATENCY);
					//System.out.println(links[i].leftID+" "+links[i].rightID+" "+(int)links[i].bandWidth);
				}
			}
			scanner.close();
			fin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(scanner!=null){
				scanner.close();
			}
		} 
	}

	public void initMatrix() {
		adjacencyMatrix = new int[nodes.length][nodes.length];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				adjacencyMatrix[i][j] = 0;
			}
		}
		for (int i = 0; i < links.length; i++) {
			adjacencyMatrix[links[i].leftID][links[i].rightID] = (int) links[i].bandWidth;
			adjacencyMatrix[links[i].rightID][links[i].leftID] = (int) links[i].bandWidth;
		}
	}
	
	public void refreshMatrix() {
		for (int i = 0; i < links.length; i++) {
			adjacencyMatrix[links[i].leftID][links[i].rightID] = (int) (links[i].bandWidth - links[i].getRate());
			adjacencyMatrix[links[i].rightID][links[i].leftID] = (int) (links[i].bandWidth - links[i].getRate());
		}
	}
	
	public int[][] getMatrix() {
		return adjacencyMatrix;
	}
	
	public int[] dijkstra(int start, int end, int bandwidth) {

		int NODE_NUM=adjacencyMatrix.length;
		int[][] paths = new int[NODE_NUM - 1][NODE_NUM];
		for (int i = 0; i < NODE_NUM - 1; i++) {
			for (int j = 0; j < NODE_NUM; j++) {
				paths[i][j] = -1;
			}
		}

		boolean[] flag = new boolean[NODE_NUM];
		for (int i = 0; i < flag.length; i++) {
			flag[i] = true;
		}

		int[] prev = new int[NODE_NUM];
		for (int i = 0; i < prev.length; i++) {
			prev[i] = 0;
		}

		paths[0][0] = start;
		flag[start] = false;
		prev[0] = 1;
		int pathNumStart = 0, pathNumEnd = 1;

		for (int i = 0; i < NODE_NUM; i++) {
			for (int j = pathNumStart; j < pathNumEnd; j++) {
				if (adjacencyMatrix[paths[j][prev[j] - 1]][end] >= bandwidth) {
					paths[j][prev[j]++] = end;
					int[] ret = new int[prev[j]];
					System.arraycopy(paths[j], 0, ret, 0, prev[j]);
					for (int k = 1; k < ret.length; k++) {
						adjacencyMatrix[ret[k-1]][ret[k]]-=bandwidth;
						adjacencyMatrix[ret[k]][ret[k-1]]-=bandwidth;
					}
					return ret;
				} else {
					int pathPoint = pathNumEnd;
					for (int k = 0; k < NODE_NUM; k++) {
						if (flag[k] && adjacencyMatrix[paths[j][prev[j] - 1]][k] >= bandwidth) {
							flag[k] = false;
							System.arraycopy(paths[j], 0, paths[pathPoint], 0, prev[j]);
							paths[pathPoint][prev[j]] = k;
							prev[pathPoint++] = prev[j] + 1;
						}
					}
					pathNumStart = pathNumEnd;
					pathNumEnd = pathPoint;
				}
			}
		}
		return null;
	}
	
	public JSONObject getGraph() {
		JSONArray node = new JSONArray(), link = new JSONArray();
		for (int i = 0; i < nodes.length; i++) {
			node.put(nodes[i].getNode());
		}
		for (int i = 0; i < links.length; i++) {
			link.put(links[i].getLink());
		}
		try {
			JSONObject json = new JSONObject("{\"nodes\":" + node + ",\"links\":" + link + "}");
			return json;
		} catch (JSONException e) {
		}
		return null;
	}
}
