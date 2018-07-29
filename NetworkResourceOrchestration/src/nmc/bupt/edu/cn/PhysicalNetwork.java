package nmc.bupt.edu.cn;

import java.util.Random;

public class PhysicalNetwork {
	public static int nodeCompute = 100;  //节点计算资源量
	public static int linkCapacity = 100;      //链路带宽资源量

	public int linkDelay() {
		Random r = new Random();
		int delay = r.nextInt(15) % (11) + 5;
		return delay;
	}
}

class SmallNetwork extends PhysicalNetwork {
	public static int[] endPoints = { -1, 0, 9, 5 }; // 用户端点直接相连的物理节点
	public static int nodeNum = 14;
	public static int[] nodesCompute = new int[nodeNum];
	public static int[][] linksCapacity = new int[nodeNum][nodeNum];
	public static int[][] linksDelay = new int[nodeNum][nodeNum];

	public SmallNetwork() {
		for (int i = 0; i < nodeNum; i++) {
			nodesCompute[i] = nodeCompute;
		}

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksCapacity[i][j] = 0;
				} else
					linksCapacity[i][j] = -1;
			}
		}
		linksCapacity[0][1] = linksCapacity[1][0] = linkCapacity;
		linksCapacity[0][2] = linksCapacity[2][0] = linkCapacity;
		linksCapacity[0][13] = linksCapacity[13][0] = linkCapacity;
		linksCapacity[1][2] = linksCapacity[2][1] = linkCapacity;
		linksCapacity[2][3] = linksCapacity[3][2] = linkCapacity;
		linksCapacity[2][13] = linksCapacity[13][2] = linkCapacity;
		linksCapacity[3][4] = linksCapacity[4][3] = linkCapacity;
		linksCapacity[3][5] = linksCapacity[5][3] = linkCapacity;
		linksCapacity[3][6] = linksCapacity[6][3] = linkCapacity;
		linksCapacity[3][7] = linksCapacity[7][3] = linkCapacity;
		linksCapacity[3][10] = linksCapacity[10][3] = linkCapacity;
		linksCapacity[4][5] = linksCapacity[5][4] = linkCapacity;
		linksCapacity[5][6] = linksCapacity[6][5] = linkCapacity;
		linksCapacity[6][7] = linksCapacity[7][6] = linkCapacity;
		linksCapacity[7][8] = linksCapacity[8][7] = linkCapacity;
		linksCapacity[7][10] = linksCapacity[10][7] = linkCapacity;
		linksCapacity[8][9] = linksCapacity[9][8] = linkCapacity;
		linksCapacity[8][10] = linksCapacity[10][8] = linkCapacity;
		linksCapacity[9][10] = linksCapacity[10][9] = linkCapacity;
		linksCapacity[9][11] = linksCapacity[11][9] = linkCapacity;
		linksCapacity[10][11] = linksCapacity[11][10] = linkCapacity;
		linksCapacity[10][13] = linksCapacity[13][10] = linkCapacity;
		linksCapacity[11][12] = linksCapacity[12][11] = linkCapacity;
		linksCapacity[12][13] = linksCapacity[13][12] = linkCapacity;

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksDelay[i][j] = 0;
				} else
					linksDelay[i][j] = 1000;
			}
		}
		linksDelay[0][1] = linksDelay[1][0] = linkDelay();
		linksDelay[0][2] = linksDelay[2][0] = linkDelay();
		linksDelay[0][13] = linksDelay[13][0] = linkDelay();
		linksDelay[1][2] = linksDelay[2][1] = linkDelay();
		linksDelay[2][13] = linksDelay[13][2] = linkDelay();
		linksDelay[2][3] = linksDelay[3][2] = linkDelay();
		linksDelay[3][4] = linksDelay[4][3] = linkDelay();
		linksDelay[3][5] = linksDelay[5][3] = linkDelay();
		linksDelay[3][6] = linksDelay[6][3] = linkDelay();
		linksDelay[3][7] = linksDelay[7][3] = linkDelay();
		linksDelay[3][10] = linksDelay[10][3] = linkDelay();
		linksDelay[4][5] = linksDelay[5][4] = linkDelay();
		linksDelay[5][6] = linksDelay[6][5] = linkDelay();
		linksDelay[6][7] = linksDelay[7][6] = linkDelay();
		linksDelay[7][8] = linksDelay[8][7] = linkDelay();
		linksDelay[7][10] = linksDelay[10][7] = linkDelay();
		linksDelay[8][9] = linksDelay[9][8] = linkDelay();
		linksDelay[8][10] = linksDelay[10][8] = linkDelay();
		linksDelay[9][10] = linksDelay[10][9] = linkDelay();
		linksDelay[9][11] = linksDelay[11][9] = linkDelay();
		linksDelay[10][11] = linksDelay[11][10] = linkDelay();
		linksDelay[10][13] = linksDelay[13][10] = linkDelay();
		linksDelay[11][12] = linksDelay[12][11] = linkDelay();
		linksDelay[12][13] = linksDelay[13][12] = linkDelay();
	}
}

class MediumNetwork extends PhysicalNetwork {
	public static int[] endPoints = { -1, 0, 5, 19, 13 }; // 用户端点直接相连的物理节点
	public static int nodeNum = 22;
	public static int[] nodesCompute = new int[nodeNum];
	public static int[][] linksCapacity = new int[nodeNum][nodeNum];
	public static int[][] linksDelay = new int[nodeNum][nodeNum];

	public MediumNetwork() {
		for (int i = 0; i < nodeNum; i++) {
			nodesCompute[i] = nodeCompute;
		}

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksCapacity[i][j] = 0;
				} else
					linksCapacity[i][j] = -1;
			}
		}
		linksCapacity[0][1] = linksCapacity[1][0] = linkCapacity;
		linksCapacity[0][10] = linksCapacity[10][0] = linkCapacity;
		linksCapacity[0][11] = linksCapacity[11][0] = linkCapacity;
		linksCapacity[1][2] = linksCapacity[2][1] = linkCapacity;
		linksCapacity[1][10] = linksCapacity[10][1] = linkCapacity;
		linksCapacity[2][3] = linksCapacity[3][2] = linkCapacity;
		linksCapacity[2][9] = linksCapacity[9][2] = linkCapacity;
		linksCapacity[3][4] = linksCapacity[4][3] = linkCapacity;
		linksCapacity[3][5] = linksCapacity[5][3] = linkCapacity;
		linksCapacity[3][7] = linksCapacity[7][3] = linkCapacity;
		linksCapacity[3][8] = linksCapacity[8][3] = linkCapacity;
		linksCapacity[4][5] = linksCapacity[5][4] = linkCapacity;
		linksCapacity[5][6] = linksCapacity[6][5] = linkCapacity;
		linksCapacity[6][7] = linksCapacity[7][6] = linkCapacity;
		linksCapacity[6][19] = linksCapacity[19][6] = linkCapacity;
		linksCapacity[6][20] = linksCapacity[20][6] = linkCapacity;
		linksCapacity[7][8] = linksCapacity[8][7] = linkCapacity;
		linksCapacity[7][20] = linksCapacity[20][7] = linkCapacity;
		linksCapacity[8][9] = linksCapacity[9][8] = linkCapacity;
		linksCapacity[8][17] = linksCapacity[17][8] = linkCapacity;
		linksCapacity[8][21] = linksCapacity[21][8] = linkCapacity;
		linksCapacity[9][10] = linksCapacity[10][9] = linkCapacity;
		linksCapacity[9][16] = linksCapacity[16][9] = linkCapacity;
		linksCapacity[10][15] = linksCapacity[15][10] = linkCapacity;
		linksCapacity[11][12] = linksCapacity[12][11] = linkCapacity;
		linksCapacity[11][15] = linksCapacity[15][11] = linkCapacity;
		linksCapacity[12][13] = linksCapacity[13][12] = linkCapacity;
		linksCapacity[12][15] = linksCapacity[15][12] = linkCapacity;
		linksCapacity[13][14] = linksCapacity[14][13] = linkCapacity;
		linksCapacity[13][15] = linksCapacity[15][13] = linkCapacity;
		linksCapacity[14][15] = linksCapacity[15][14] = linkCapacity;
		linksCapacity[14][16] = linksCapacity[16][14] = linkCapacity;
		linksCapacity[15][16] = linksCapacity[16][15] = linkCapacity;
		linksCapacity[16][17] = linksCapacity[17][16] = linkCapacity;
		linksCapacity[18][19] = linksCapacity[19][18] = linkCapacity;
		linksCapacity[18][20] = linksCapacity[20][18] = linkCapacity;
		linksCapacity[19][20] = linksCapacity[20][19] = linkCapacity;
		linksCapacity[20][21] = linksCapacity[21][20] = linkCapacity;

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksDelay[i][j] = 0;
				} else
					linksDelay[i][j] = 1000;
			}
		}
		linksDelay[0][1] = linksDelay[1][0] = linkDelay();
		linksDelay[0][10] = linksDelay[10][0] = linkDelay();
		linksDelay[0][11] = linksDelay[11][0] = linkDelay();
		linksDelay[1][2] = linksDelay[2][1] = linkDelay();
		linksDelay[1][10] = linksDelay[10][1] = linkDelay();
		linksDelay[2][3] = linksDelay[3][2] = linkDelay();
		linksDelay[2][9] = linksDelay[9][2] = linkDelay();
		linksDelay[3][4] = linksDelay[4][3] = linkDelay();
		linksDelay[3][5] = linksDelay[5][3] = linkDelay();
		linksDelay[3][7] = linksDelay[7][3] = linkDelay();
		linksDelay[3][8] = linksDelay[8][3] = linkDelay();
		linksDelay[4][5] = linksDelay[5][4] = linkDelay();
		linksDelay[5][6] = linksDelay[6][5] = linkDelay();
		linksDelay[6][7] = linksDelay[7][6] = linkDelay();
		linksDelay[6][19] = linksDelay[19][6] = linkDelay();
		linksDelay[6][20] = linksDelay[20][6] = linkDelay();
		linksDelay[7][8] = linksDelay[8][7] = linkDelay();
		linksDelay[7][20] = linksDelay[20][7] = linkDelay();
		linksDelay[8][9] = linksDelay[9][8] = linkDelay();
		linksDelay[8][17] = linksDelay[17][8] = linkDelay();
		linksDelay[8][21] = linksDelay[21][8] = linkDelay();
		linksDelay[9][10] = linksDelay[10][9] = linkDelay();
		linksDelay[9][16] = linksDelay[16][9] = linkDelay();
		linksDelay[10][15] = linksDelay[15][10] = linkDelay();
		linksDelay[11][12] = linksDelay[12][11] = linkDelay();
		linksDelay[11][15] = linksDelay[15][11] = linkDelay();
		linksDelay[12][13] = linksDelay[13][12] = linkDelay();
		linksDelay[12][15] = linksDelay[15][12] = linkDelay();
		linksDelay[13][14] = linksDelay[14][13] = linkDelay();
		linksDelay[13][15] = linksDelay[15][13] = linkDelay();
		linksDelay[14][15] = linksDelay[15][14] = linkDelay();
		linksDelay[14][16] = linksDelay[16][14] = linkDelay();
		linksDelay[15][16] = linksDelay[16][15] = linkDelay();
		linksDelay[16][17] = linksDelay[17][16] = linkDelay();
		linksDelay[18][19] = linksDelay[19][18] = linkDelay();
		linksDelay[18][20] = linksDelay[20][18] = linkDelay();
		linksDelay[19][20] = linksDelay[20][19] = linkDelay();
		linksDelay[20][21] = linksDelay[21][20] = linkDelay();
	}
}

class LargeNetwork extends PhysicalNetwork {
	public static int[] endPoints = { -1, 0, 4, 12, 15, 22 }; // 用户端点直接相连的物理节点
	public static int nodeNum = 34;
	public static int[] nodesCompute = new int[nodeNum];
	public static int[][] linksCapacity = new int[nodeNum][nodeNum];
	public static int[][] linksDelay = new int[nodeNum][nodeNum];

	public LargeNetwork() {
		for (int i = 0; i < nodeNum; i++) {
			nodesCompute[i] = nodeCompute;
		}

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksCapacity[i][j] = 0;
				} else
					linksCapacity[i][j] = -1;
			}
		}
		linksCapacity[0][1] = linksCapacity[1][0] = linkCapacity;
		linksCapacity[0][2] = linksCapacity[2][0] = linkCapacity;
		linksCapacity[0][24] = linksCapacity[24][0] = linkCapacity;
		linksCapacity[1][2] = linksCapacity[2][1] = linkCapacity;
		linksCapacity[1][24] = linksCapacity[24][1] = linkCapacity;
		linksCapacity[2][3] = linksCapacity[3][2] = linkCapacity;
		linksCapacity[2][25] = linksCapacity[25][2] = linkCapacity;
		linksCapacity[3][4] = linksCapacity[4][3] = linkCapacity;
		linksCapacity[3][27] = linksCapacity[27][3] = linkCapacity;
		linksCapacity[4][5] = linksCapacity[5][4] = linkCapacity;
		linksCapacity[4][6] = linksCapacity[6][4] = linkCapacity;
		linksCapacity[4][28] = linksCapacity[28][4] = linkCapacity;
		linksCapacity[5][6] = linksCapacity[6][5] = linkCapacity;
		linksCapacity[6][7] = linksCapacity[7][6] = linkCapacity;
		linksCapacity[6][30] = linksCapacity[30][6] = linkCapacity;
		linksCapacity[6][31] = linksCapacity[31][6] = linkCapacity;
		linksCapacity[7][8] = linksCapacity[8][7] = linkCapacity;
		linksCapacity[7][10] = linksCapacity[10][7] = linkCapacity;
		linksCapacity[7][33] = linksCapacity[33][7] = linkCapacity;
		linksCapacity[8][9] = linksCapacity[9][8] = linkCapacity;
		linksCapacity[9][10] = linksCapacity[10][9] = linkCapacity;
		linksCapacity[10][11] = linksCapacity[11][10] = linkCapacity;
		linksCapacity[10][12] = linksCapacity[12][10] = linkCapacity;
		linksCapacity[10][13] = linksCapacity[13][10] = linkCapacity;
		linksCapacity[10][33] = linksCapacity[33][10] = linkCapacity;
		linksCapacity[11][12] = linksCapacity[12][11] = linkCapacity;
		linksCapacity[12][13] = linksCapacity[13][12] = linkCapacity;
		linksCapacity[13][14] = linksCapacity[14][13] = linkCapacity;
		linksCapacity[14][15] = linksCapacity[15][14] = linkCapacity;
		linksCapacity[14][33] = linksCapacity[33][14] = linkCapacity;
		linksCapacity[15][16] = linksCapacity[16][15] = linkCapacity;
		linksCapacity[15][17] = linksCapacity[17][15] = linkCapacity;
		linksCapacity[15][32] = linksCapacity[32][15] = linkCapacity;
		linksCapacity[16][17] = linksCapacity[17][16] = linkCapacity;
		linksCapacity[16][18] = linksCapacity[18][16] = linkCapacity;
		linksCapacity[17][18] = linksCapacity[18][17] = linkCapacity;
		linksCapacity[17][32] = linksCapacity[18][32] = linkCapacity;
		linksCapacity[18][19] = linksCapacity[19][18] = linkCapacity;
		linksCapacity[18][19] = linksCapacity[19][18] = linkCapacity;
		linksCapacity[19][20] = linksCapacity[20][19] = linkCapacity;
		linksCapacity[19][21] = linksCapacity[21][19] = linkCapacity;
		linksCapacity[20][21] = linksCapacity[21][20] = linkCapacity;
		linksCapacity[20][27] = linksCapacity[27][20] = linkCapacity;
		linksCapacity[20][29] = linksCapacity[29][20] = linkCapacity;
		linksCapacity[20][32] = linksCapacity[32][20] = linkCapacity;
		linksCapacity[21][22] = linksCapacity[22][21] = linkCapacity;
		linksCapacity[21][26] = linksCapacity[26][21] = linkCapacity;
		linksCapacity[22][23] = linksCapacity[23][22] = linkCapacity;
		linksCapacity[22][26] = linksCapacity[26][22] = linkCapacity;
		linksCapacity[23][24] = linksCapacity[24][23] = linkCapacity;
		linksCapacity[23][25] = linksCapacity[25][23] = linkCapacity;
		linksCapacity[24][25] = linksCapacity[25][24] = linkCapacity;
		linksCapacity[25][26] = linksCapacity[26][25] = linkCapacity;
		linksCapacity[25][27] = linksCapacity[27][25] = linkCapacity;
		linksCapacity[27][28] = linksCapacity[28][27] = linkCapacity;
		linksCapacity[28][29] = linksCapacity[29][28] = linkCapacity;
		linksCapacity[29][30] = linksCapacity[30][29] = linkCapacity;
		linksCapacity[30][31] = linksCapacity[31][30] = linkCapacity;
		linksCapacity[31][32] = linksCapacity[32][31] = linkCapacity;
		linksCapacity[31][33] = linksCapacity[33][31] = linkCapacity;

		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				if (i == j) {
					linksDelay[i][j] = 0;
				} else
					linksDelay[i][j] = 1000;
			}
		}
		linksDelay[0][1] = linksDelay[1][0] = linkDelay();
		linksDelay[0][2] = linksDelay[2][0] = linkDelay();
		linksDelay[0][24] = linksDelay[24][0] = linkDelay();
		linksDelay[1][2] = linksDelay[2][1] = linkDelay();
		linksDelay[1][24] = linksDelay[24][1] = linkDelay();
		linksDelay[2][3] = linksDelay[3][2] = linkDelay();
		linksDelay[2][25] = linksDelay[25][2] = linkDelay();
		linksDelay[3][4] = linksDelay[4][3] = linkDelay();
		linksDelay[3][27] = linksDelay[27][3] = linkDelay();
		linksDelay[4][5] = linksDelay[5][4] = linkDelay();
		linksDelay[4][6] = linksDelay[6][4] = linkDelay();
		linksDelay[4][28] = linksDelay[28][4] = linkDelay();
		linksDelay[5][6] = linksDelay[6][5] = linkDelay();
		linksDelay[6][7] = linksDelay[7][6] = linkDelay();
		linksDelay[6][30] = linksDelay[30][6] = linkDelay();
		linksDelay[6][31] = linksDelay[31][6] = linkDelay();
		linksDelay[7][8] = linksDelay[8][7] = linkDelay();
		linksDelay[7][10] = linksDelay[10][7] = linkDelay();
		linksDelay[7][33] = linksDelay[33][7] = linkDelay();
		linksDelay[8][9] = linksDelay[9][8] = linkDelay();
		linksDelay[9][10] = linksDelay[10][9] = linkDelay();
		linksDelay[10][11] = linksDelay[11][10] = linkDelay();
		linksDelay[10][12] = linksDelay[12][10] = linkDelay();
		linksDelay[10][13] = linksDelay[13][10] = linkDelay();
		linksDelay[10][33] = linksDelay[33][10] = linkDelay();
		linksDelay[11][12] = linksDelay[12][11] = linkDelay();
		linksDelay[12][13] = linksDelay[13][12] = linkDelay();
		linksDelay[13][14] = linksDelay[14][13] = linkDelay();
		linksDelay[14][15] = linksDelay[15][14] = linkDelay();
		linksDelay[14][33] = linksDelay[33][14] = linkDelay();
		linksDelay[15][16] = linksDelay[16][15] = linkDelay();
		linksDelay[15][17] = linksDelay[17][15] = linkDelay();
		linksDelay[15][32] = linksDelay[32][15] = linkDelay();
		linksDelay[16][17] = linksDelay[17][16] = linkDelay();
		linksDelay[16][18] = linksDelay[18][16] = linkDelay();
		linksDelay[17][18] = linksDelay[18][17] = linkDelay();
		linksDelay[17][32] = linksDelay[18][32] = linkDelay();
		linksDelay[18][19] = linksDelay[19][18] = linkDelay();
		linksDelay[18][19] = linksDelay[19][18] = linkDelay();
		linksDelay[19][20] = linksDelay[20][19] = linkDelay();
		linksDelay[19][21] = linksDelay[21][19] = linkDelay();
		linksDelay[20][21] = linksDelay[21][20] = linkDelay();
		linksDelay[20][27] = linksDelay[27][20] = linkDelay();
		linksDelay[20][29] = linksDelay[29][20] = linkDelay();
		linksDelay[20][32] = linksDelay[32][20] = linkDelay();
		linksDelay[21][22] = linksDelay[22][21] = linkDelay();
		linksDelay[21][26] = linksDelay[26][21] = linkDelay();
		linksDelay[22][23] = linksDelay[23][22] = linkDelay();
		linksDelay[22][26] = linksDelay[26][22] = linkDelay();
		linksDelay[23][24] = linksDelay[24][23] = linkDelay();
		linksDelay[23][25] = linksDelay[25][23] = linkDelay();
		linksDelay[24][25] = linksDelay[25][24] = linkDelay();
		linksDelay[25][26] = linksDelay[26][25] = linkDelay();
		linksDelay[25][27] = linksDelay[27][25] = linkDelay();
		linksDelay[27][28] = linksDelay[28][27] = linkDelay();
		linksDelay[28][29] = linksDelay[29][28] = linkDelay();
		linksDelay[29][30] = linksDelay[30][29] = linkDelay();
		linksDelay[30][31] = linksDelay[31][30] = linkDelay();
		linksDelay[31][32] = linksDelay[32][31] = linkDelay();
		linksDelay[31][33] = linksDelay[33][31] = linkDelay();
	}
}