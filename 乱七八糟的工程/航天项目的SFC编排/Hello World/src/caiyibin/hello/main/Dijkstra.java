package caiyibin.hello.main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Dijkstra {
	private static String pathTip = "-->";
	private static Map<String, Object> pathInfoMap = new LinkedHashMap<String, Object>(); // 路径和距离信息

	// 计算所有节点之间的最短距离
	public static void getPathInfo(int[][] weight, int start, String[] point) {
		// 接受一个有向图的权重矩阵，和一个起点编号start（从0编号，顶点存在数组中）
		int n = weight.length; // 顶点个数
		int[] shortPath = new int[n]; // 保存start到其他各点的最短路径的距离
		int[] visited = new int[n]; // 标记当前该顶点的最短路径是否已经求出,1表示已求出
		String[] path = new String[n]; // 保存start到其他各点最短路径的字符串表示
		for (int i = 0; i < n; i++) {
			path[i] = new String(point[start] + pathTip + point[i]);
		}
		// 初始化，第一个顶点已经求出
		shortPath[start] = 0;
		visited[start] = 1;
		for (int count = 1; count < n; count++) { // 要加入n-1个顶点
			int k = -1; // 选出一个距离初始顶点start最近的未标记顶点
			int dmin = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			// 将新选出的顶点标记为已求出最短路径，且到start的最短路径就是dmin
			shortPath[k] = dmin;
			visited[k] = 1;
			// 以k为中间点，修正从start到未访问各点的距离
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = weight[start][k] + weight[k][i];
					path[i] = path[k] + pathTip + point[i];
				}
			}
		}

		for (int i = 0; i < n; i++) {
			// System.out.print("从" + point[start] + "出发到" + point[i] +"的最短路径为："
			// + path[i] + " ");
			// System.out.println("从" + point[start] + "出发到" + point[i]
			// +"的最短距离为：" + shortPath[i]);
			Object[] objects = new Object[2];
			objects[0] = path[i];
			objects[1] = shortPath[i];
			pathInfoMap.put(point[start] + pathTip + point[i], objects);
		}
	}

	// 打印路径信息和距离
	public static int printPathInfo(String src, String dst, int[] retpath) {
		int forwardingDelay = 0;
		for (Entry<String, Object> entry : pathInfoMap.entrySet()) {
			String key = entry.getKey();
			Object[] objects = (Object[]) entry.getValue();
			// 可以打印出所有节点之间的最短路径
			// System.out.println(key + ":" + objects[0] + " 路径长度：" +
			// objects[1]);
			// 只打印所求的节点之间的路径
			if (key.startsWith(src) && key.endsWith(dst)) {
				// 记录路径上的每个节点
				String[] tmp = ((String) objects[0]).split("-->");
				for (int i = 0; i < tmp.length; i++) {
					retpath[i] = Integer.parseInt(tmp[i]);
				}
				System.out.println("在MEO上的转发路径为：" + key + ": " + objects[0]);
				System.out.println("在该路径上的转发时延为：" + objects[1] + " ms");
				forwardingDelay = (int) objects[1];
			}
		}
		return forwardingDelay; // 此路径的转发时延
	}

	// 获得所有节点之间最短路径
	public static void getAllShortestPath() {
		for (int start = 0; start < SCNclass.point.length; start++) {
			getPathInfo(SCNclass.MEOpathDelay, start, SCNclass.point);
		}
	}
}