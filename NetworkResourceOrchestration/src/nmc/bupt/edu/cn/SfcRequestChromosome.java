package nmc.bupt.edu.cn;

import java.util.Random;
import java.util.Vector;

public class SfcRequestChromosome implements Cloneable {

	public SmallNetwork smallNetwork; // ȷ����������
	public MediumNetwork mediumNetwork;
	public LargeNetwork largeNetwork;
	public int[] endPoints; // ȷ����ʼ��Ŀ�Ľڵ�
	public int networkType; // �������������
	public int geneNum; // ��������ڵ���
	public int sfcType; // SFC���������
	public int chromLen; // SFC�����VNF�ڵ����������������㷨��ͬ��
	public int[] geneSequence; // Ⱦɫ���ڵĻ������У�����ڵ����У�
	public double fitness; // ��Ӧ��

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

	// ����������ն˽ڵ�����(��ͬ������û��ն˽ڵ㲻ͬ)
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

	// �����Ŵ��㷨�ĳ�ʼ�����
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

	// ������Ӧ�Ⱥ���
	public double calculateFitness() {
		double processDelayWeight = 1.0; // ����ʱ��Ȩ�ز���
		double processDelay = 0.0; // ����ʱ��
		double transDelayWeight = 1.0; // ����ʱ��Ȩ�ز���
		double transDelay = 0.0; // ����ʱ��
		double totalDelayWeight = 1.0; // ��ʱ��Ȩ��
		double totalDelay = 0.0; // ��ʱ��

		double nodeWeight = 1.0; // �ڵ㿪��Ȩ�ز���
		double nodeCost = 0.0; // �ڵ㿪��
		double linkWeight = 1.0; // ��·����Ȩ�ز���
		double linkCost = 0.0; // ��·����
		double totalCostWeight = 1.0; // �ܿ���Ȩ�ز���
		double totalCost = 0.0; // �ܿ���

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
		// Ϊ����SFC׼����
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
			 * ���Ŵ��㷨������Ӧ��ʱ���Ȳ�Ҫ������Դ�ˣ�Ҳ������ʾ·�� tmpPathArray =
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
			 * ���Ŵ��㷨������Ӧ��ʱ���Ȳ�Ҫ������Դ�ˣ�Ҳ������ʾ·�� tmpPathArray =
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
			 * ���Ŵ��㷨������Ӧ��ʱ���Ȳ�Ҫ������Դ�ˣ�Ҳ������ʾ����Ľڵ�
			 * System.out.println("���Բ���VNFa�Ľڵ��У�"+geneSequence[0]);
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
			// System.out.print("����VNFa��VNFb�Ľڵ��ǣ�"+geneSequence[0] + "
			// "+geneSequence[1]);
			processDelay += SfcUtil.processDelay();
			processDelay += SfcUtil.processDelay();

			totalDelay = (processDelayWeight * processDelay + transDelayWeight * transDelay);
			break;
		case 3:
			// ��һ��·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("����VNFa��VNFb��VNFc�Ľڵ��ǣ�"+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[4]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], endPoints[2]);
			// System.out.println("����VNFc��VNFd�Ľڵ��ǣ�"+geneSequence[3]+"
			// "+geneSequence[4]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 4:
			// ��һ��·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("����VNFa��VNFb��VNFd�Ľڵ��ǣ�"+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], endPoints[2]);
			// System.out.println("����VNFc�Ľڵ��ǣ�"+geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 5:
			// ��һ��·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("����VNFa��VNFb��VNFc�Ľڵ��ǣ�"+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], endPoints[1]);
			// System.out.println("����VNFc�Ľڵ��ǣ�"+geneSequence[3]);
			for (int i = 0; i < 3; i++) {
				processDelay2 += SfcUtil.processDelay();
			}

			totalDelay2 = (processDelayWeight * processDelay2 + transDelayWeight * transDelay2);

			totalDelay = Math.max(totalDelay1, totalDelay2);
			break;
		case 6:
			// ��һ��·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[1]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[1], geneSequence[2]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[2], endPoints[1]);
			// System.out.println("����VNFa��VNFb��VNFd�Ľڵ��ǣ�"+geneSequence[0]+"
			// "+geneSequence[1]+" "+geneSequence[2]);
			for (int i = 0; i < 3; i++) {
				processDelay1 += SfcUtil.processDelay();
			}

			totalDelay1 = (processDelayWeight * processDelay1 + transDelayWeight * transDelay1);
			// �ڶ���·
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, endPoints[0], geneSequence[0]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[0], geneSequence[3]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[3], geneSequence[4]);
			transDelay += Dijkstra.dijkstraPathDelay(calculateFitnessDelayMatrix, geneSequence[4], endPoints[1]);
			// System.out.println("����VNFc��VNFd�Ľڵ��ǣ�"+geneSequence[3]+"
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
		//System.out.print("��ʱ�ӣ�" + totalDelay + " " + "�ڵ���Դ���ģ�" + "" + "��·��Դ���ģ�" + " ");
		fitness = totalDelay * totalDelayWeight + totalCost * totalCostWeight;
		return fitness;
	}

	public void print() {
		System.out.print("SFC����ڵ�ӳ�������ڵ�����Ϊ��");
		for (int i = 0; i < chromLen; i++) {
			System.out.print(geneSequence[i] + " ");
		}
		System.out.println();
		System.out.println("��Ӧ��Ϊ�� " + getFitness());
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
