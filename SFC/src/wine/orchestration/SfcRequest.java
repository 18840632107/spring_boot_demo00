package wine.orchestration;

import java.util.Random;
import java.util.Vector;

import random.orchestration.Dijkstra;
import random.orchestration.SfcUtil;

public class SfcRequest implements Cloneable {

	public SmallNetwork smallNetwork; // 确定网络类型
	public MediumNetwork mediumNetwork;
	public LargeNetwork largeNetwork;
	public int[] endPoints; // 确定起始与目的节点
	public int networkType; // 物理网络的类型
	public int geneNum; // 物理网络节点数
	public int sfcType; // SFC请求的类型
	public int chromLen; // SFC请求的节点数
	public int[] geneSequence; // 染色体内的基因序列（物理节点序列）
	public double fitness; // 适应度

	public SfcRequest(int networkType, int sfcType) {
		this.smallNetwork = main.smallNetwork;
		this.mediumNetwork = main.mediumNetwork;
		this.largeNetwork = main.largeNetwork;

		this.networkType = networkType;
		switch (networkType) {
		case 1:
			this.geneNum = SmallNetwork.nodeNum;
			this.endPoints = endPointsGeneration(SmallNetwork.endPoints);
			break;
		case 2:
			this.geneNum = MediumNetwork.nodeNum;
			this.endPoints = endPointsGeneration(MediumNetwork.endPoints);
			break;
		case 3:
			this.geneNum = LargeNetwork.nodeNum;
			this.endPoints = endPointsGeneration(LargeNetwork.endPoints);
			break;
		default:
			break;
		}

		this.sfcType = sfcType;
		switch (sfcType) {
		case 1:
			this.chromLen = 3;
			break;
		case 2:
			this.chromLen = 4;
			break;
		case 3:
			this.chromLen = 8;
			break;
		case 4:
			this.chromLen = 7;
			break;
		case 5:
			this.chromLen = 6;
			break;
		case 6:
			this.chromLen = 7;
			break;
		default:
			break;
		}
		this.geneSequence = new int[this.chromLen];
		this.fitness = 0.0;
	}

	public int[] getGeneSequence() {
		return geneSequence;
	}

	public void setGeneSequence(int[] geneSequence) {
		this.geneSequence = geneSequence;
	}

	public double getFitness() {
		this.fitness = calculateFitness();
		return fitness;
	}

	// 产生随机的终端节点数组(不同网络的用户终端节点不同)
	public int[] endPointsGeneration(int[] clientNode) {
		Vector<Integer> endPoints = new Vector<Integer>();
		for (int i = 1; i < clientNode.length; i++) {
			endPoints.add(clientNode[i]);
		}
		int[] res = new int[3];
		for (int i = 0; i < 3; i++) {
			Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
			int index = r.nextInt(endPoints.size());
			res[i] = endPoints.get(index).intValue();
			endPoints.remove(index);
		}
		return res;
	}

	// 产生WiNE解，结果与最短路径解几乎一样（性能可能会差一点），所以不写了！
	/* 基本思想：
	 * 1. 为每一个VNF筛选出可部署的物理节点集合，记为VNF1、VNF2...
	 * 2. 按照各自集合元素个数，将VNF降序排列
	 * 3. 对排列好的VNF进行映射，找出映射相邻VNF的最小链路开销的物理节点对（一一匹配查找）
	 */
	public void wineGeneration() {
		geneSequence[0] = endPoints[0]; // 起点
		geneSequence[chromLen - 1] = endPoints[1]; // 终点（之一）
		if (sfcType == 3 || sfcType == 4) {
			geneSequence[chromLen - 2] = endPoints[2];
		}
		
		// 确定部署VNF的节点,geneSequence[]的元素
		if (networkType == 1) {
			int[] tmpPathArray1 = null;
			int[] tmpPathArray2 = null;
			int countNodeNum = 0;

			switch (sfcType) {
			case 1:
				Vector<Integer> VNF1 = new Vector<Integer>();				
				for(int i=0;i<smallNetwork.nodeNum;i++){ 
					int tmp = (geneSequence[0]+i)%smallNetwork.nodeNum;
					if (SfcUtil.nodeComsuption(SfcUtil.smallInstanceNodeComsuption) < this.smallNetwork.nodesCompute[tmp]) {
						VNF1.add(Integer.valueOf(tmp));
					}
				}
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				break;
			case 2:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				break;
			// 讲道理，应该增加约束，同一SFC中的VNF不能放置在同一节点上，这里不加了，麻烦
			case 3:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// 第二条路的起点是SFC中分叉点VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[1],
						geneSequence[chromLen - 2]);
				
				break;
			case 4:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// 第二条路的起点是SFC中分叉点VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[2],
						geneSequence[chromLen - 2]);
				
				break;
			case 5:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// 第二条路的起点是SFC中分叉点VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[2],
						geneSequence[chromLen - 1]);
				
				break;
			case 6:
				
				tmpPathArray1 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[0],
						geneSequence[chromLen - 1]);
				
				// 第二条路的起点是SFC中分叉点VNF
				
				tmpPathArray2 = Dijkstra.dijkstraPathArray(this.smallNetwork.linksDelay, geneSequence[1],
						geneSequence[chromLen - 1]);
				
				break;
			default:
				break;
			}
		}

	}

	// 计算适应度函数
	public double calculateFitness() {
		double processDelayWeight = 1.0; // 处理时延权重参数
		double processDelay = 0.0; // 处理时延
		double transDelayWeight = 1.0; // 传播时延权重参数
		double transDelay = 0.0; // 传播时延
		double totalDelayWeight = 1.0; // 总时延权重
		double totalDelay = 0.0; // 总时延

		double nodeWeight = 1.0; // 节点开销权重参数
		double nodeCost = 0.0; // 节点开销
		double linkWeight = 1.0; // 链路开销权重参数
		double linkCost = 0.0; // 链路开销
		double totalCostWeight = 1.0; // 总开销权重参数
		double totalCost = 0.0; // 总开销

		double fitness = 0.0;
		int[][] calculateFitnessDelayMatrix = null;

		switch (networkType) {
		case 1:
			calculateFitnessDelayMatrix = this.smallNetwork.linksDelay;
			break;
		case 2:
			calculateFitnessDelayMatrix = this.mediumNetwork.linksDelay;
			break;
		case 3:
			calculateFitnessDelayMatrix = this.largeNetwork.linksDelay;
			break;
		default:
			break;
		}

		int[] tmpPathArray = null;
		// 为复杂SFC准备的
		double processDelay1 = 0.0;
		double processDelay2 = 0.0;
		double transDelay1 = 0.0;
		double transDelay2 = 0.0;
		double totalDelay1 = 0.0;
		double totalDelay2 = 0.0;

		switch (sfcType) {
		case 1:
			for (int i = 0; i < geneSequence.length - 1; i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			System.out.println("可以部署VNFa的节点有：" + geneSequence[1]);
			this.smallNetwork.nodesCompute[geneSequence[1]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 2:
			for (int i = 0; i < geneSequence.length - 1; i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			System.out.print("部署VNFa与VNFb的节点是：" + geneSequence[1] + " " + geneSequence[2]);
			this.smallNetwork.nodesCompute[geneSequence[1]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();
			this.smallNetwork.nodesCompute[geneSequence[2]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 3:
			// 第一条路
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[7]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[7]);

			System.out
					.println("部署VNFa、VNFb与VNFc的节点是：" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			for (int i = 4; i < (6); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("部署VNFc与VNFd的节点是：" + geneSequence[4] + " " + geneSequence[5]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			this.smallNetwork.nodesCompute[geneSequence[5]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 4:
			// 第一条路
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);

			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}
			System.out
					.println("部署VNFa、VNFb与VNFd的节点是：" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			for (int i = 0; i < (2); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("可以部署VNFc的节点有：" + geneSequence[4]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 5:
			// 第一条路
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[5]);
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			System.out
					.println("部署VNFa、VNFb与VNFc的节点是：" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			for (int i = 0; i < (2); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], geneSequence[5]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("可以部署VNFd的节点有：" + geneSequence[4]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 6:
			// 第一条路
			for (int i = 0; i < (3); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			transDelay1 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[6]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}

			System.out
					.println("部署VNFa、VNFb与VNFd的节点是：" + geneSequence[1] + " " + geneSequence[2] + " " + geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
				this.smallNetwork.nodesCompute[geneSequence[i + 1]] -= SfcUtil
						.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[4]);
			for (int j = 0; j < tmpPathArray.length - 1; j++) {
				linkCost = SfcUtil.linkComsuption();
				this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
				this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
			}
			for (int i = 4; i < (6); i++) {
				tmpPathArray = Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
				for (int j = 0; j < tmpPathArray.length - 1; j++) {
					linkCost = SfcUtil.linkComsuption();
					this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j + 1]] -= linkCost;
					this.smallNetwork.linksCapacity[tmpPathArray[j + 1]][tmpPathArray[j]] -= linkCost;
				}
				transDelay2 += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[i],
						geneSequence[i + 1]);
			}

			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}
			System.out.println("部署VNFc与VNFd的节点是：" + geneSequence[4] + " " + geneSequence[5]);
			this.smallNetwork.nodesCompute[geneSequence[4]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			this.smallNetwork.nodesCompute[geneSequence[5]] -= SfcUtil
					.nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		default:
			break;
		}
		System.out.print("总时延：" + totalDelay + " " + "节点资源消耗：" + "" + "链路资源消耗：" + " ");
		fitness = totalDelay * totalDelayWeight + totalCost * totalCostWeight;
		return fitness;
	}

	// 得到WiNE解的编排结果
	public void getWiNEOrchestrationResult() {
		wineGeneration();

		System.out.print("SFC请求节点映射的物理节点序列为：");
		for (int i = 0; i < chromLen; i++) {
			System.out.print(geneSequence[i] + " ");
		}
		System.out.println();
		System.out.println("适应度为： " + getFitness());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SfcRequest chromosome = (SfcRequest) super.clone();
		chromosome.chromLen = this.chromLen;
		chromosome.geneSequence = this.geneSequence.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}
}
