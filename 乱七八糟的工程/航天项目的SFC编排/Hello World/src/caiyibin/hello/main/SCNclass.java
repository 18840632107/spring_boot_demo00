package caiyibin.hello.main;

import java.util.Random;

public class SCNclass {
	// 初始化地面网关节点资源，LEO网节点资源与MEO网节点资源与链路资源
	public static int numofGW = 32;
	public static int numofLEOSatellites = 30;
	public static int numofMEOSatellites = 64;
	public static Hub[] HG = new Hub[numofGW];
	public static satLEO[] satL = new satLEO[numofLEOSatellites];
	public static satMEO[] satM = new satMEO[numofMEOSatellites];
	public static int[][] MEOpathDelay = new int[numofMEOSatellites][numofMEOSatellites];
	public static int[][] MEOpathBW = new int[numofMEOSatellites][numofMEOSatellites];
	public static int failGW = 0;
	public static int failLEO = 0;
	public static int failMEO = 0;
	public static int failMEOBW = 0;
	public static int cmsMax = 30;
	public static int cmsMin = 5;
	public static int rlMax = 60;
	public static int rlMin = 5;
	// point目前也是根据25个节点初始化的，获得所有最短路径
	public static String[] point = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
			"33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
			"51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64" };

	public static void initial() {
		// 初始化地面网关核心网络
		for (int i = 0; i < numofGW; i++) {
			HG[i] = new Hub(i);
		}
		// 初始化LEO网(一共四组，其中一组长这样)
		// X 0 X
		// 1 4 2
		// X 3 X
		for (int i = 0; i < numofLEOSatellites; i++) {
			satL[i] = new satLEO(i);
		}
		satL[numofLEOSatellites - 1].bandWidthLM = 3200.0;
		// 初始化MEO网
		for (int i = 0; i < numofMEOSatellites; i++) {
			satM[i] = new satMEO(i);
		}
		// 目前是根据25个节点初始化的MEO网时延、带宽情况
		// 依据的是IS系统的约等比例扩大，轨道高度为6000km，12条轨道，每条轨道16个卫星
		for (int i = 0; i < numofMEOSatellites; i++) {
			for (int j = 0; j < numofMEOSatellites; j++) {
				int tmp = Math.max(i, j);
				if ((tmp % 8 != 0) && (Math.abs(i - j) == 1)) {
					Random r = new Random();
					MEOpathDelay[i][j] = r.nextInt(17) % (4) + 14;
					MEOpathBW[i][j] = 100;
				} else if (Math.abs(i - j) == 8) { // 同一轨道内的链路时延
					MEOpathDelay[i][j] = 15;
					MEOpathBW[i][j] = 100;
				} else {
					MEOpathDelay[i][j] = 1000; // 不存在的链路的时延设为很大且无带宽资源
					MEOpathBW[i][j] = -1;
				}
			}
		}
		MEOpathDelay[7][8] = 1000;
		MEOpathDelay[8][7] = 1000;
		MEOpathDelay[15][16] = 1000;
		MEOpathDelay[16][15] = 1000;
		MEOpathDelay[23][24] = 1000;
		MEOpathDelay[24][23] = 1000;
		MEOpathDelay[31][32] = 1000;
		MEOpathDelay[32][31] = 1000;
		MEOpathDelay[39][40] = 1000;
		MEOpathDelay[40][39] = 1000;
		MEOpathDelay[47][48] = 1000;
		MEOpathDelay[48][47] = 1000;
		MEOpathDelay[55][56] = 1000;
		MEOpathDelay[56][55] = 1000;
	}

	// 在地面核心网的GW上部署“big function”
	public static double deployOnHUB(Hub[] HG) {
		double hubDelay = 0.0;
		Random r = new Random();
		int GW = r.nextInt(numofGW);
		int com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
		if (HG[GW].CPU >= com && HG[GW].Memory >= com && HG[GW].Storage >= com && HG[GW].bandWidthHL >= com) {
			com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
			HG[GW].CPU -= com;
			com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
			HG[GW].Memory -= com;
			com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
			HG[GW].Storage -= com;
			com = r.nextInt(rlMax) % (rlMax - rlMin + 1) + rlMin;
			HG[GW].bandWidthHL -= com;
			hubDelay += (HG[GW].bandDelayHL + r.nextInt(2));
			int process = r.nextInt(5) + 5;
			hubDelay += process;
		} else {
			failGW++;
			System.out.println("此次SFC在GW上部署失败");
		}
		System.out.println("在GW上部署“big function”的总时延为：" + hubDelay + "ms");
		return hubDelay;
	}

	// 在LEO上部署radio，一共分为四组，每组4个LEO。如，第一组是0,1,2 到 3; 但是我们这里让9个非边界LEO在一组里，一个边界LEO
	public static double deployOnLEO(satLEO[] satL) {
		double leoDelay = 0.0;
		Random r = new Random();
		int com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
		int process = r.nextInt(5); // 处理时延
		int LEOpath = r.nextInt(numofLEOSatellites);
		if (satL[LEOpath].CPU >= com && satL[LEOpath].Radio >= com) {
			com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
			satL[LEOpath].CPU -= com;
			com = r.nextInt(rlMax) % (rlMax - rlMin + 1) + rlMin;
			satL[LEOpath].Radio -= com;
			com = r.nextInt(rlMax) % (rlMax - rlMin + 1) + rlMin;
			satL[LEOpath].bandWidthLL -= com;
			leoDelay += satL[LEOpath].TerLeoDelay;
			leoDelay += satL[LEOpath].LeoLeoDelay;
			leoDelay += process;
		} else {
			failLEO++;
			System.out.println("此次SFC在LEO上部署失败");
		}
		com = r.nextInt(rlMax) % (rlMax - rlMin + 1) + rlMin;
		satL[numofLEOSatellites - 1].bandWidthLM -= com;
		leoDelay += satL[11].LeoMeoDelay;
		System.out.println("在LEO上部署radio VNF的总时延为：" + leoDelay + "ms");
		return leoDelay;
	}

	// 在MEO上部署on-board VNF
	public static double deployOnMEO(satMEO[] satM, int[] retpath) {
		double meoDelay = 0.0;
		Random r = new Random();
		int com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
		int i = 1;
		int numOfNodeInPath = 0;
		for (int j = 1; j < retpath.length; j++) {
			if (retpath[i] != 0) {
				numOfNodeInPath++;
			}
		}
		while (retpath[i] != 0) {
			if (satM[retpath[i]].CPU >= com && satM[retpath[i]].Memory >= com && satM[retpath[i]].Storage >= com) {
				com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
				satM[retpath[i]].CPU -= com;
				com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
				satM[retpath[i]].Memory -= com;
				com = r.nextInt(cmsMax) % (cmsMax - cmsMin + 1) + cmsMin;
				satM[retpath[i]].Storage -= com;
				int process = r.nextInt(5);
				meoDelay += process;
				break;
			} else {
				i++;
				numOfNodeInPath--;
			}
		}
		boolean flag = true;
		if (numOfNodeInPath == 0) {
			failMEO++;
			System.out.println("此次SFC在MEO上部署失败");
		} else {
			i = 0;
			while (retpath[i + 1] != 0) {
				com = r.nextInt(rlMax) % (rlMax - rlMin + 1) + rlMin;
				if (MEOpathBW[retpath[i]][retpath[i + 1]] >= com) {
					MEOpathBW[retpath[i]][retpath[i + 1]] -= com;
					i++;
				} else if (MEOpathBW[retpath[i]][retpath[i + 1]] < com) {
					failMEOBW++;
					flag = false;
					System.out.println("此次SFC在MEO上部署失败(带宽不足)");
					break;
				}
			}
		}
		if (flag == true) {
			System.out.println("在该路径部署VNF的处理时延为：" + meoDelay + " ms");
		} else
			meoDelay = 0;
		return meoDelay;
	}
}

// 地面核心网的GW类
class Hub {
	public int hubId = 0; // 0--numofGW
	public double CPU = 0.0;
	public double Memory = 0.0;
	public double Storage = 0.0;
	public double bandWidthHL = 0.0;
	public double bandDelayHL = 0.0;

	Hub(int hubId) {
		this.hubId = hubId;
		double r = 100.0;
		this.CPU = r;
		this.Memory = r;
		this.Storage = r;
		this.bandWidthHL = r;
		this.bandDelayHL = 4.0;
	}
};

// LEO卫星类
class satLEO {
	public int leoId = 0;// 0...numofLEO
	public double CPU = 0.0;
	public double Radio = 0.0;
	public double bandWidthLM = 0.0;
	public double bandWidthLL = 0.0;
	public double TerLeoDelay = 0.0;
	public double LeoMeoDelay = 0.0;
	public double LeoLeoDelay = 0.0;

	satLEO(int leoId) {
		this.leoId = leoId;
		double r = 100.0;
		this.CPU = r;
		this.Radio = r;
		this.bandWidthLL = r;
		this.bandWidthLM = r;
		this.TerLeoDelay = 3.0; // 780km的高空
		this.LeoMeoDelay = 17.0; // 780km与6000km之差
		this.LeoLeoDelay = 12.0; // 以轨道内ISL计算
	}
};

// MEO卫星类
class satMEO {
	public int meoId = 0; // 0--numofMEO
	public double CPU = 0.0;
	public double Memory = 0.0;
	public double Storage = 0.0;
	public double MeoMeoDelay = 0.0;

	satMEO(int meoId) {
		this.meoId = meoId;
		double r = 100.0;
		this.CPU = r;
		this.Memory = r;
		this.Storage = r;
		this.MeoMeoDelay = 15.0; // 6000km高空的卫星轨道
	}
};
