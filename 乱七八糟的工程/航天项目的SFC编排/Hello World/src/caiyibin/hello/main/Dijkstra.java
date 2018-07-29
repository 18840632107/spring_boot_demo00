package caiyibin.hello.main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Dijkstra {
	private static String pathTip = "-->";
	private static Map<String, Object> pathInfoMap = new LinkedHashMap<String, Object>(); // ·���;�����Ϣ

	// �������нڵ�֮�����̾���
	public static void getPathInfo(int[][] weight, int start, String[] point) {
		// ����һ������ͼ��Ȩ�ؾ��󣬺�һ�������start����0��ţ�������������У�
		int n = weight.length; // �������
		int[] shortPath = new int[n]; // ����start��������������·���ľ���
		int[] visited = new int[n]; // ��ǵ�ǰ�ö�������·���Ƿ��Ѿ����,1��ʾ�����
		String[] path = new String[n]; // ����start�������������·�����ַ�����ʾ
		for (int i = 0; i < n; i++) {
			path[i] = new String(point[start] + pathTip + point[i]);
		}
		// ��ʼ������һ�������Ѿ����
		shortPath[start] = 0;
		visited[start] = 1;
		for (int count = 1; count < n; count++) { // Ҫ����n-1������
			int k = -1; // ѡ��һ�������ʼ����start�����δ��Ƕ���
			int dmin = Integer.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			// ����ѡ���Ķ�����Ϊ��������·�����ҵ�start�����·������dmin
			shortPath[k] = dmin;
			visited[k] = 1;
			// ��kΪ�м�㣬������start��δ���ʸ���ľ���
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = weight[start][k] + weight[k][i];
					path[i] = path[k] + pathTip + point[i];
				}
			}
		}

		for (int i = 0; i < n; i++) {
			// System.out.print("��" + point[start] + "������" + point[i] +"�����·��Ϊ��"
			// + path[i] + " ");
			// System.out.println("��" + point[start] + "������" + point[i]
			// +"����̾���Ϊ��" + shortPath[i]);
			Object[] objects = new Object[2];
			objects[0] = path[i];
			objects[1] = shortPath[i];
			pathInfoMap.put(point[start] + pathTip + point[i], objects);
		}
	}

	// ��ӡ·����Ϣ�;���
	public static int printPathInfo(String src, String dst, int[] retpath) {
		int forwardingDelay = 0;
		for (Entry<String, Object> entry : pathInfoMap.entrySet()) {
			String key = entry.getKey();
			Object[] objects = (Object[]) entry.getValue();
			// ���Դ�ӡ�����нڵ�֮������·��
			// System.out.println(key + ":" + objects[0] + " ·�����ȣ�" +
			// objects[1]);
			// ֻ��ӡ����Ľڵ�֮���·��
			if (key.startsWith(src) && key.endsWith(dst)) {
				// ��¼·���ϵ�ÿ���ڵ�
				String[] tmp = ((String) objects[0]).split("-->");
				for (int i = 0; i < tmp.length; i++) {
					retpath[i] = Integer.parseInt(tmp[i]);
				}
				System.out.println("��MEO�ϵ�ת��·��Ϊ��" + key + ": " + objects[0]);
				System.out.println("�ڸ�·���ϵ�ת��ʱ��Ϊ��" + objects[1] + " ms");
				forwardingDelay = (int) objects[1];
			}
		}
		return forwardingDelay; // ��·����ת��ʱ��
	}

	// ������нڵ�֮�����·��
	public static void getAllShortestPath() {
		for (int start = 0; start < SCNclass.point.length; start++) {
			getPathInfo(SCNclass.MEOpathDelay, start, SCNclass.point);
		}
	}
}