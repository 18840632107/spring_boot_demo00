package nmc2.bupt.edu.cn;

import java.util.*;

class Switch {
	private int id; // 0 -- (5 * K * K) / 4 - 1

	public Switch(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

class Server {
	private int id; // ��������Ž�����������ţ� (5 * K * K) / 4 -- allNodes-1
	private int serverId; // ������������
	private int coreNum = 4; // Ĭ���ĺ˴��������ĸ��������
	private int[] sfcIdInCore = new int[coreNum]; // ÿ���˱��ĸ�SFCռ��
	private int[] vnfTypeInCore = new int[coreNum]; // ÿ���˽�Ҫ���е�VNF����
	private double[] cpuCoreRes = new double[coreNum]; // ÿ���˵ļ�����Դ��

	public Server(int id) {
		this.id = id;
		for (int i = 0; i < coreNum; i++) {
			sfcIdInCore[i] = -1;
			vnfTypeInCore[i] = -1;
			cpuCoreRes[i] = 100.0;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getCoreNum() {
		return coreNum;
	}

	public void setCoreNum(int coreNum) {
		this.coreNum = coreNum;
	}

	public int getSfcIdInCore(int coreIndex) {
		return sfcIdInCore[coreIndex];
	}

	public void setSfcIdInCore(int coreIndex, int sfcId) {
		this.sfcIdInCore[coreIndex] = sfcId;
	}

	public int getVnfTypeInCore(int coreIndex) {
		return vnfTypeInCore[coreIndex];
	}

	public void setVnfTypeInCore(int coreIndex, int vnfType) {
		this.vnfTypeInCore[coreIndex] = vnfType;
	}

	public double getCpuCoreRes(int coreIndex) {
		return cpuCoreRes[coreIndex];
	}

	public void setCpuCoreRes(int coreIndex, double cpuCoreRes) {
		this.cpuCoreRes[coreIndex] = cpuCoreRes;
	}

}

public class DataCenterNetwork {
	private int K;
	private int allNodes; // �������ܽ����Ϊ�����������Ϸ�������
	private ArrayList<Switch> switchList = new ArrayList<Switch>();
	private ArrayList<Server> serverList = new ArrayList<Server>();
	private double[][] linksBandWidth; // �ڽӾ����ʾ����
	private double[][] linksDelay; // ʱ��

	public DataCenterNetwork(int K) {
		this.K = K;
		generateFatTreeDC(this.K);
	}

	public void generateFatTreeDC(int K) {
		setAllNodes(K);
		linksBandWidth = new double[getAllNodes()][getAllNodes()];
		linksDelay = new double[getAllNodes()][getAllNodes()];
		for (int i = 0; i < allNodes; i++) {
			for (int j = 0; j < allNodes; j++) {
				if (i == j) {
					linksBandWidth[i][j] = 0; // ͬ�ڵ�֮����Ϊ�޴�����ʱ�ò�����
				} else
					linksBandWidth[i][j] = -1;
			}
		}
		for (int i = 0; i < allNodes; i++) {
			for (int j = 0; j < allNodes; j++) {
				if (i == j) {
					linksDelay[i][j] = 0; // ͬ�ڵ�֮����Ϊ��ʱ�ӣ���ʱ�ò�����
				} else
					linksDelay[i][j] = 1000;
			}
		}
		// ȷ���������������
		int cs = 0;
		int ce = (K / 2) * (K / 2) - 1;
		int as = ce + 1;
		int ae = as + (K * K / 2) - 1;
		int es = ae + 1;
		int ee = es + (K * K / 2) - 1;
		// ȷ����������������ı��
		int ss = ee + 1;
		// int se = ss + (K * K * K) / 4 - 1;

		for (int i = cs; i <= ee; i++)
			switchList.add(new Switch(i));
		for (int i = (ee + 1); i < ((ee + 1) + K * K * K / 4); i++)
			serverList.add(new Server(i));
		// ��ʼ�����Ĳ���ۺϲ�֮�����·����
		final int caBandWidth = 10000;
		for (int i = 0; i <= ce; i++) {
			int tmp = i / (K / 2);
			for (int j = (as + tmp); j <= ae; j += (K / 2)) {
				linksBandWidth[i][j] = caBandWidth;
				linksBandWidth[j][i] = caBandWidth;
				double caDelay = new Random().nextDouble() * 10.0 % 10.0 + 1;
				linksDelay[i][j] = caDelay;
				linksDelay[j][i] = caDelay;
			}
		}
		// �ۺϲ���ܶ���
		final int aeBandWidth = 1000;
		for (int i = as; i <= ae; i++) {
			int tmp = (i - as) / (K / 2);
			for (int j = es + tmp * (K / 2); j < es + tmp * (K / 2) + K / 2; j++) {
				linksBandWidth[i][j] = aeBandWidth;
				linksBandWidth[j][i] = aeBandWidth;
				double aeDelay = new Random().nextDouble() * 20.0 % 10.0 + 10.0;
				linksDelay[i][j] = aeDelay;
				linksDelay[j][i] = aeDelay;
			}
		}
		// �ܶ���������
		final int esBandWidth = 100;
		int serverNum = ss;
		for (int i = es; i <= ee; i++) {
			for (int j = 0; j < K / 2; j++) {
				linksBandWidth[i][serverNum] = esBandWidth;
				linksBandWidth[serverNum][i] = esBandWidth;
				double esDelay = new Random().nextDouble() * 30.0 % 10.0 + 20.0;
				linksDelay[i][serverNum] = esDelay;
				linksDelay[serverNum][i] = esDelay;
				serverList.get(serverNum - (ee + 1)).setServerId(serverNum - (ee + 1));
				serverNum++;
			}
		}
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(int K) {
		this.allNodes = (5 * K * K / 4) + (K * K * K / 4);
	}

	public ArrayList<Switch> getSwitchList() {
		return switchList;
	}

	public void setSwitchList(ArrayList<Switch> switchList) {
		this.switchList = switchList;
	}

	public ArrayList<Server> getServerList() {
		return serverList;
	}

	public void setServerList(ArrayList<Server> serverList) {
		this.serverList = serverList;
	}

	public double[][] getLinksBandWidth() {
		return linksBandWidth;
	}

	public void setLinksBandWidth(double[][] linksBandWidth) {
		this.linksBandWidth = linksBandWidth;
	}

	public double[][] getLinksDelay() {
		return linksDelay;
	}

	public void setLinksDelay(double[][] linksDelay) {
		this.linksDelay = linksDelay;
	}
}
