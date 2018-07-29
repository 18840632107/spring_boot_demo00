package nmc.bupt.edu.cn;

import java.util.Random;
import java.util.Vector;

public class SfcRequestChromosome implements Cloneable {

	public SmallNetwork smallNetwork; // 确定网络类型
	public MediumNetwork mediumNetwork;
	public LargeNetwork largeNetwork;
	public int[] endPoints; // 确定起始与目的节点
	public int networkType; // 物理网络的类型
	public int geneNum; // 物理网络节点数
	public int sfcType; // SFC请求的类型
	public int chromLen; // SFC请求的VNF节点数（这里与其他算法不同）
	public int[] geneSequence; // 染色体内的基因序列（物理节点序列）
	public double fitness; // 适应度

	public SfcRequestChromosome(int networkType, int sfcType) {
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
			this.chromLen = 1;
			break;
		case 2:
			this.chromLen = 2;
			break;
		case 3:
			this.chromLen = 5;
			break;
		case 4:
			this.chromLen = 4;
			break;
		case 5:
			this.chromLen = 4;
			break;
		case 6:
			this.chromLen = 5;
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

	// 生成遗传算法的初始随机解
	public void randomGeneration() {
		Vector<Integer> allowedGenes = new Vector<Integer>();
		for (int i = 0; i < geneNum; i++) {
			allowedGenes.add(Integer.valueOf(i));
		}
		for (int i = 0; i < chromLen; i++) {
			Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
			int index = r.nextInt(allowedGenes.size());
			geneSequence[i] = allowedGenes.get(index).intValue();
			allowedGenes.remove(index);
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
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			/*
			 * 在遗传算法计算适应度时就先不要消耗资源了，也不用显示路径 tmpPathArray =
			 * Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix,
			 * endPoints[0], geneSequence[0]); for(int
			 * j=0;j<tmpPathArray.length-1;j++){
			 * linkCost=SfcUtil.linkComsuption();
			 * this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j+1
			 * ]]-=linkCost;
			 * this.smallNetwork.linksCapacity[tmpPathArray[j+1]][tmpPathArray[j
			 * ]]-=linkCost; }
			 */
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], endPoints[1]);
			/*
			 * 在遗传算法计算适应度时就先不要消耗资源了，也不用显示路径 tmpPathArray =
			 * Dijkstra.dijkstraPathArray(calculateFitnessDelayMatrix,
			 * geneSequence[0], endPoints[1]); for(int
			 * j=0;j<tmpPathArray.length-1;j++){
			 * linkCost=SfcUtil.linkComsuption();
			 * this.smallNetwork.linksCapacity[tmpPathArray[j]][tmpPathArray[j+1
			 * ]]-=linkCost;
			 * this.smallNetwork.linksCapacity[tmpPathArray[j+1]][tmpPathArray[j
			 * ]]-=linkCost; }
			 */
			/*
			 * 在遗传算法计算适应度时就先不要消耗资源了，也不用显示部署的节点
			 * System.out.println("可以部署VNFa的节点有："+geneSequence[0]);
			 * this.smallNetwork.nodesCompute[geneSequence[0]]-=SfcUtil.
			 * nodeComsuption(SfcUtil.smallInstanceNodeComsuption);
			 */
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 2:
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], endPoints[1]);
			// System.out.print("部署VNFa与VNFb的节点是："+geneSequence[0] + "
			// "+geneSequence[1]);
			processDelay += SfcUtil.processDelay();
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 3:
			// 第一条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("部署VNFa、VNFb与VNFc的节点是："+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[4]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], endPoints[2]);
			// System.out.println("部署VNFc与VNFd的节点是："+geneSequence[3]+"
			// "+geneSequence[4]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 4:
			// 第一条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("部署VNFa、VNFb与VNFd的节点是："+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], endPoints[2]);
			// System.out.println("部署VNFc的节点是："+geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 5:
			// 第一条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("部署VNFa、VNFb与VNFc的节点是："+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], endPoints[1]);
			// System.out.println("部署VNFc的节点是："+geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 6:
			// 第一条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("部署VNFa、VNFb与VNFd的节点是："+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// 第二条路
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[4]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], endPoints[1]);
			// System.out.println("部署VNFc与VNFd的节点是："+geneSequence[3]+"
			// "+geneSequence[4]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		default:
			break;
		}
		//System.out.print("总时延：" + totalDelay + " " + "节点资源消耗：" + "" + "链路资源消耗：" + " ");
		fitness = totalDelay * totalDelayWeight + totalCost * totalCostWeight;
		return fitness;
	}

	public void print() {
		System.out.print("SFC请求节点映射的物理节点序列为：");
		for (int i = 0; i < chromLen; i++) {
			System.out.print(geneSequence[i] + " ");
		}
		System.out.println();
		System.out.println("适应度为： " + getFitness());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SfcRequestChromosome chromosome = (SfcRequestChromosome) super.clone();
		chromosome.chromLen = this.chromLen;
		chromosome.geneSequence = this.geneSequence.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}
}
