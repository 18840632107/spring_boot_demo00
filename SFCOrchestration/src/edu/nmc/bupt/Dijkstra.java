package edu.nmc.bupt;

public class Dijkstra {
	public static int[] dijkstraPathArray(double[][] weight1, int start, int end) {
		double[][] weight = new double[weight1.length][weight1[0].length];
		for(int i=0;i<weight1.length;i++){
			for(int j=0;j<weight1[0].length;j++){
				weight[i][j]=weight1[i][j];
			}
		}
		// ����һ������ͼ��Ȩ�ؾ��󣬺�һ�������start����0��ţ�������������У�
		// ����һ��int[] ���飬��ʾ��start���������·������
		int n = weight.length; // �������
		int[] shortPath = new int[n]; // ����start��������������·��
		String[] path = new String[n]; // ����start�������������·�����ַ�����ʾ
		for (int i = 0; i < n; i++)
			path[i] = new String(start + "-->" + i);
		int[] visited = new int[n]; // ��ǵ�ǰ�ö�������·���Ƿ��Ѿ����,1��ʾ�����
		// ��ʼ������һ�������Ѿ����
		shortPath[start] = 0;
		visited[start] = 1;
		for (int count = 1; count < n; count++) { // Ҫ����n-1������
			int k = -1; // ѡ��һ�������ʼ����start�����δ��Ƕ���
			double dmin = Double.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			// ����ѡ���Ķ�����Ϊ��������·�����ҵ�start�����·������dmin
			shortPath[k] = (int)dmin;
			visited[k] = 1;
			// ��kΪ�м�㣬������start��δ���ʸ���ľ���
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = weight[start][k] + weight[k][i];
					path[i] = path[k] + "-->" + i;
				}
			}
		}
		// ��·����Ϣת��Ϊ��������
		String[] pathStr = path[end].split("-->");
		int[] pathArray = new int[pathStr.length];
		for (int i = 0; i < pathStr.length; i++) {
			pathArray[i] = Integer.parseInt(pathStr[i]);
		}
		System.out.println("��" + start + "������" + end + "�����·��Ϊ��" + path[end]);
		return pathArray;
	}

	public static int dijkstraPathDelay(int[][] weight1, int start, int end) {
		double[][] weight = new double[weight1.length][weight1[0].length];
		for(int i=0;i<weight1.length;i++){
			for(int j=0;j<weight1[0].length;j++){
				weight[i][j]=weight1[i][j];
			}
		}		
		// ����һ������ͼ��Ȩ�ؾ��󣬺�һ�������start����0��ţ�������������У�
		// ����һ��int[] ���飬��ʾ��start���������·������
		int n = weight.length; // �������
		int[] shortPath = new int[n]; // ����start��������������·��
		String[] path = new String[n]; // ����start�������������·�����ַ�����ʾ
		for (int i = 0; i < n; i++)
			path[i] = new String(start + "-->" + i);
		int[] visited = new int[n]; // ��ǵ�ǰ�ö�������·���Ƿ��Ѿ����,1��ʾ�����
		// ��ʼ������һ�������Ѿ����
		shortPath[start] = 0;
		visited[start] = 1;
		for (int count = 1; count < n; count++) { // Ҫ����n-1������
			int k = -1; // ѡ��һ�������ʼ����start�����δ��Ƕ���
			double dmin = Double.MAX_VALUE;
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][i] < dmin) {
					dmin = weight[start][i];
					k = i;
				}
			}
			// ����ѡ���Ķ�����Ϊ��������·�����ҵ�start�����·������dmin
			shortPath[k] = (int) dmin;
			visited[k] = 1;
			// ��kΪ�м�㣬������start��δ���ʸ���ľ���
			for (int i = 0; i < n; i++) {
				if (visited[i] == 0 && weight[start][k] + weight[k][i] < weight[start][i]) {
					weight[start][i] = weight[start][k] + weight[k][i];
					path[i] = path[k] + "-->" + i;
				}
			}
		}
		// ��·����Ϣת��Ϊ��������
		String[] pathStr = path[end].split("-->");
		int[] pathArray = new int[pathStr.length];
		for (int i = 0; i < pathStr.length; i++) {
			pathArray[i] = Integer.parseInt(pathStr[i]);
		}
		System.out.println("��" + start + "������" + end + "���������Ϊ��" + shortPath[end]);
		return shortPath[end];
	}
}