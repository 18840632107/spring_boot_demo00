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
	private int id; // 服务器编号接续交换机编号， (5 * K * K) / 4 -- allNodes-1
	private int serverId; // 服务器自身编号
	private int coreNum = 4; // 默认四核处理器（四个虚拟机）
	private int[] sfcIdInCore = new int[coreNum]; // 每个核被哪个SFC占用
	private int[] vnfTypeInCore = new int[coreNum]; // 每个核将要运行的VNF类型
	private double[] cpuCoreRes = new double[coreNum]; // 每个核的计算资源量

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
	private int allNodes; // 网络中总结点数为交换机数加上服务器数
	private ArrayList<Switch> switchList = new ArrayList<Switch>();
	private ArrayList<Server> serverList = new ArrayList<Server>();
	private double[][] linksBandWidth; // 邻接矩阵表示带宽
	private double[][] linksDelay; // 时延

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
					linksBandWidth[i][j] = 0; // 同节点之间认为无带宽（暂时用不到）
				} else
					linksBandWidth[i][j] = -1;
			}
		}
		for (int i = 0; i < allNodes; i++) {
			for (int j = 0; j < allNodes; j++) {
				if (i == j) {
					linksDelay[i][j] = 0; // 同节点之间认为无时延（暂时用不到）
				} else
					linksDelay[i][j] = 1000;
			}
		}
		// 确定胖树交换机编号
		int cs = 0;
		int ce = (K / 2) * (K / 2) - 1;
		int as = ce + 1;
		int ae = as + (K * K / 2) - 1;
		int es = ae + 1;
		int ee = es + (K * K / 2) - 1;
		// 确定胖树服务器自身的编号
		int ss = ee + 1;
		// int se = ss + (K * K * K) / 4 - 1;

		for (int i = cs; i <= ee; i++)
			switchList.add(new Switch(i));
		for (int i = (ee + 1); i < ((ee + 1) + K * K * K / 4); i++)
			serverList.add(new Server(i));
		// 初始化核心层与聚合层之间的链路矩阵
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
		// 聚合层与架顶层
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
		// 架顶层与主机
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
